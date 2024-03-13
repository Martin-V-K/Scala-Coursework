# Scala-Coursework
 
# BASIC FOOD BASKET PRICES ANALYSIS APPLICATION

In this assignment you are required to develop an application in Scala to extract information from
data on basic food prices. You will be provided with (fictional) average monthly prices (in pence),
over a period of 2 years, for a set of basic food.

Your application should allow the user to select from the set of analyses indicated below, and obtain
the results of the chosen analysis. The application should have a simple text-based interface which
includes a menu to allow the user to make their selections.

## Data

The data is supplied to you as a comma-separated text file data.txt. Each line of the file contains the
food symbol (i.e. TOMATO) and 24 numeric values. These values represent average monthly prices
(in pence), most recent last, for a kg (or litre) of the food item. A sample line from the file is:
TOMATO, 232, 188, 251, 316, 162, 168, 287, 174, 243,…
Your application should read the file contents and store the data in a map structure where each line
of the file is used to construct a map entry with the stock symbol as the key, and a list of integers as
the value. 

The type of the structure should be Map[String, List[Int]]. The sample line shown
should be represented within the map as follows:
Map("TOMATO" -> List(232, 188, 251, 316, 162, 168, 287, 174, 243,…),
… )
You will also be supplied with a fragment of Scala code, in a ScalaWorksheet file data.sc, that
creates a map with the same format, containing equivalent data. You can use this, if you wish, to test
other functions as required without the need to have completed the file reading functionality.

## Analyses

Your application should allow the user to perform the following analyses:
1. Get current (most recent) price for each food.
2. Get the highest and lowest prices within the period for each food.
3. Get the median price over the period for each food.
4. Get the symbol for the food which has risen most over the last 6 months, that is, has shown
the largest rise between the last 6 months of the period and the 6 months earlier.
5. Compare the average values over the 2-year period of two foods selected by the user.
6. Allow the user to input a food basket and show its total value based on the current (most
recent) values of the foods.
• Each basket entry is a food symbol and the number of kg/litres (as a float value).
• The basket should be stored in another map structure, e.g. Map("RICE" -> 2.5,
"BEEF" -> 0.5, "APPLE" -> 1).
• Any food symbol not recognized will be ignored (and a warning message printed).

## Application structure

The application should be implemented as a Scala singleton object, with the food data map as a
field. Your code should firstly read the file data into the map. It should then display a menu, allow
the user to input a choice, and invoke a suitable handler function for that choice. The menu should
have an option for each of the required analyses, and also an option to quit the application.
For each analysis it is suggested that you define the following:

• A function that performs the required operation on the data and returns the results to be
displayed.
• A function, to be invoked by a menu option, that accepts user input if required for an
operation, invokes the operation function, and displays the results of the operation to the
user.
• The functions for each analysis should be composed to perform the operation and display
the result. For example, analysis 1 could be performed using:
o A function that is applied to the data and returns a result of type Map[String, Int].
o A function that invokes the operation and iterates through the resulting map to
display its contents.

Each analysis will differ in terms of the user input required and the type of result returned by the
operation.
An excellent mark will be awarded in each case for an implemented solution that completely
addresses the requirement, produces fully correct output, is well-structured, and makes appropriate
use of functional programming techniques. Lower marks will be awarded where the solution is
lacking in any of these aspects.

## Testing

You should test your operation functions using the test data - for each one you should define input
data if required, note the result returned by the function and compare this to the expected result.
You should test your complete application by invoking each menu option using suitable user input
and comparing the displayed result with the expected result.

## Evaluation

You should evaluate your solution with regard to the following criteria. In each case, you may refer
to specific examples within your code to support your evaluation and provide the relevant code
fragments in your report:

• **Functional thinking**: to what extent does your solution consistently evidence the application
of functional principles?
• **Functional programming style**: what functional programming techniques have you used in
your solution and why did you choose each of these, particularly where another functional
technique could have been used as an alternative.
• **Comparison of functional and imperative style**: what, in your view, did your use of
programming styles contribute to the ease of development and/or the quality of your
solution? How would you approach this assignment if given a free choice of programming
language (other than Scala)?
