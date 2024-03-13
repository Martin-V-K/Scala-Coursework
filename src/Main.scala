import scala.io.Source
import scala.io.StdIn.readInt
import scala.collection.immutable.ListMap

object Main extends App {

  val mapdata = readFile("data.txt")
  //initialLoading() // Just for testing purposes to show that the file was read correctly

  val actionMap = Map[Int, () => Boolean](1 -> handleOne, 2 -> handleTwo, 3 -> handleThree
    , 4 -> handleFour, 5 -> handleFive, 6 -> handleSix, 7 -> handleSeven)

  var option = 0
  do {
    option = readOption
  } while (menu(option))


  // *******************************************************************************************************************
  // FUNCTIONS FOR MENU AND LOADING DATA

  def readOption: Int = {
    println(
      """|Please select one of the following:

         |  1 - Get current price for each food
         |  2 - Get highest and lowest prices withing the last 24 months for each food
         |  3 - Get median price for each food
         |  4 - Get the food with the biggest rise in price
         |  5 - Compare 2 foods by average price
         |  6 - Calculate Food Basket
         |  7 - Quit
         """.stripMargin)
    readInt()
  }

  def menu(option: Int): Boolean = {
    actionMap.get(option) match {
      case Some(f) => f()
      case None =>
        println("Sorry, that command is not recognized\n")
        true
    }
  }

  /**  Just for testing purposes to show that the file was read correctly

  def initialLoading(): Unit = {
    println("Map Data:")
    mapdata.foreach { case (foodItem, values) =>
      println(s"$foodItem -> ${values.mkString("", ", ", "")}")
    }
  }

  */

  def readFile(filePath: String): ListMap[String, List[Int]] = {
    var mapdata: ListMap[String, List[Int]] = ListMap()
    try {
      val source = Source.fromFile(filePath)
      mapdata = ListMap.from(source
        .getLines()
        .map(line => line.split(",").map(_.trim))
        .map {
          case Array(foodItem, values@_*) =>
            foodItem -> values.map(_.toInt).toList
        }
      )
      source.close()
    }
    catch {
      case e: Exception =>
        println(s"An error occurred: ${e.getMessage}")
    }
    mapdata
  }

  // *******************************************************************************************************************
  // FUNCTIONS FOR OPTION ONE

  def handleOne(): Boolean = {
    menuShowLastValues(getLastValues)
    true
  }

  def getLastValues(): Map[String, Any] = {
    mapdata.view.mapValues(values => values.lastOption.getOrElse("N/A")).to(ListMap)
  }

  def menuShowLastValues(f: () => Map[String, Any]): Unit = {
    println()
    f() foreach { case (x, y) => println(s"$x: $y") }
    println()
  }

  // *******************************************************************************************************************
  // FUNCTIONS FOR OPTION TWO

  def handleTwo(): Boolean = {
    menuShowMinMaxPrices(getMinMaxPrices)
    true
  }

  def getMinMaxPrices(): Map[String, (Any, Any)] = {
    mapdata.view.mapValues(values => {
      val highestPrice = values.max
      val lowestPrice = values.min
      (highestPrice, lowestPrice)
    }).to(ListMap)
  }

  def menuShowMinMaxPrices(f: () => Map[String, (Any, Any)]): Unit = {
    println()
    f() foreach { case (food, (highestPrice, lowestPrice)) =>
      println(s"$food: Highest Price - $highestPrice, Lowest Price - $lowestPrice")
    }
    println()
  }

  // *******************************************************************************************************************
  // FUNCTIONS FOR OPTION THREE

  def handleThree(): Boolean = {
    menuShowMedianPrices(getMedianPrices)
    true
  }

  def getMedianPrices(): Map[String, Any] = {
    mapdata.view.mapValues(values => {
      val sortedValues = values.sorted
      val length = sortedValues.length

      if (length % 2 == 0) {
        val midValue1 = sortedValues((length - 1) / 2)
        val midValue2 = sortedValues(length / 2)
        (midValue1 + midValue2) / 2.0
      }
      else {
        sortedValues(length / 2).toDouble
      }
    }).to(ListMap)
  }

  def menuShowMedianPrices(f: () => Map[String, Any]): Unit = {
    println()
    f() foreach { case (food, medianPrice) =>
      println(s"$food: Median Price - $medianPrice")
    }
    println()
  }

  // *******************************************************************************************************************
  // FUNCTIONS FOR OPTION FOUR

  def handleFour(): Boolean = {
    menuShowMaxAverageRiseFood(getFoodWithMaxAverageRise)
    true
  }

  def getFoodWithMaxAverageRise(): Option[(String, Any)] = {
    val averageRiseMap = mapdata.view.mapValues(values => {
      val last12Months = values.takeRight(12)
      val (precedingSixMonths, recentSixMonths) = last12Months.splitAt(6)

      val averageRecent = recentSixMonths.sum.toDouble / 6
      val averagePreceding = precedingSixMonths.sum.toDouble / 6

      averageRecent - averagePreceding
    }).to(ListMap)

    val (food, maxAverageRise) = averageRiseMap.maxBy { case (_, value) => value }
    val roundedMaxAverageRise = BigDecimal(maxAverageRise).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
    Some(food -> roundedMaxAverageRise)
  }

  def menuShowMaxAverageRiseFood(f: () => Option[(String, Any)]): Unit = {
    println()
    f() match {
      case Some((food, maxAverage)) =>
        println(s"The food with the largest average rise over the last 6 months is: $food with a rise of $maxAverage")
      case None =>
        println("No data available.")
    }
    println()
  }


  // *******************************************************************************************************************
  // FUNCTIONS FOR OPTION FIVE

  def handleFive(): Boolean = {
    menuCompareAverageValues()
    true
  }

  def calculateAverageValues(food1: String, food2: String): (Double, Double) = {
    val values1 = mapdata.getOrElse(food1, List.empty[Int])
    val values2 = mapdata.getOrElse(food2, List.empty[Int])

    if (values1.isEmpty || values2.isEmpty) {
      (Double.NaN, Double.NaN)
    }
    else {
      val average1 = values1.sum.toDouble / values1.length
      val average2 = values2.sum.toDouble / values2.length
      (average1, average2)
    }
  }

  def printComparisonResult(food1: String, food2: String, average1: Double, average2: Double): Unit = {
    if (average1.isNaN || average2.isNaN) {
      println("\nWarning: One or both foods not found.\n")
    }
    else {
      println(s"\nAverage value for $food1 -> $average1")
      println(s"Average value for $food2 -> $average2\n")

      val comparisonResult =
        if (average1 > average2) s"$food1 has a higher average value for the last 24 months."
        else if (average1 < average2) s"$food2 has a higher average value for the last 24 months."
        else "Both foods have the same average value for the last 24 months."

      println(comparisonResult + "\n")
    }
  }

  def menuCompareAverageValues(): Unit = {
    val food1 = scala.io.StdIn.readLine("Enter the first food: ").toUpperCase()
    val food2 = scala.io.StdIn.readLine("Enter the second food: ").toUpperCase()

    val (average1, average2) = calculateAverageValues(food1, food2)
    val roundedAverage1 = if (average1.isNaN) average1 else BigDecimal(average1).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
    val roundedAverage2 = if (average2.isNaN) average2 else BigDecimal(average2).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
    printComparisonResult(food1, food2, roundedAverage1, roundedAverage2)
  }

  // *******************************************************************************************************************
  // FUNCTIONS FOR OPTION SIX

  def handleSix(): Boolean = {
    menuCalculateBasketValue()
    true
  }

  def calculateBasketValue(basket: Map[String, Double]): Double = {
    val (totalValue, unrecognisedFoods) = basket.foldLeft((0.0, List.empty[String])) {
      case ((acc, unrecognisedFoods), (food, quantity)) =>
        mapdata.get(food) match {
          case Some(prices) =>
            val mostRecentPrice = prices.lastOption.getOrElse(0)
            (acc + mostRecentPrice * quantity, unrecognisedFoods)
          case None =>
            println(s"\nWarning: Unrecognised food '$food', skipping.")
            (acc, unrecognisedFoods :+ food)
        }
    }

    if (unrecognisedFoods.nonEmpty) {
      println(s"\nWarning: Basket contains unrecognised foods: ${unrecognisedFoods.mkString(", ")}.")
    }

    if (totalValue.isNaN) {
      println("Error: Basket contains only unrecognised foods.\n")
      0.0
    }
    else {
      totalValue
    }
  }

  def printBasketValue(basketValue: Double): Unit = {
    println(s"\nTotal basket value: $basketValue\n")
  }

  def menuCalculateBasketValue(): Unit = {
    val basketInput = scala.io.StdIn.readLine("Enter the food basket (e.g., 'RICE 2.5 BEEF 0.5 APPLE 1'): ")

    val basket = basketInput.split(" ").grouped(2).collect { case Array(food, quantity) => food.toUpperCase() -> quantity.toDouble }.toMap

    val basketValue = calculateBasketValue(basket)

    printBasketValue(basketValue)
  }

  // *******************************************************************************************************************
  // HANDLE QUIT

  def handleSeven(): Boolean = {
    println("Selected Quit")
    false
  }
}