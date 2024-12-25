package concepts.part2.advanced_fp

/**
 * **Partial Functions in Scala**
 *
 * Partial functions are a special type of function which are only defined for a subset of inputs.
 */

object PartialFunctions extends App {

  /**
   * **Custom Exception**
   * Used to demonstrate a function that is not applicable for certain inputs.
   */
  class FunctionNotApplicableException extends RuntimeException

  /**
   * **Regular Function**
   * Takes an `Int` and returns `Int`. It works for all inputs.
   */
  val aFunction = (x: Int) => x + 1 // Function[Int, Int] === Int => Int

  /**
   * **Fussy Function**
   * Only handles specific inputs and throws an exception for others.
   */
  val aFussyFunction = (x: Int) => {
    if (x == 1) 42
    else if (x == 2) 56
    else if (x == 5) 999
    else throw new FunctionNotApplicableException
  }

  /**
   * **Using Pattern Matching**
   * Rewriting the fussy function with pattern matching.
   */
  val aNicerFussyFunction = (x: Int) => x match {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  }

  /**
   * **Partial Function**
   * A function only defined for certain inputs using `case`.
   */
  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  }

  // Using the partial function
  println(aPartialFunction(2)) // Output: 56
  // println(aPartialFunction(12345)) // Would throw a MatchError

  // Utilities of PartialFunction
  println(aPartialFunction.isDefinedAt(2)) // Output: true
  println(aPartialFunction.isDefinedAt(200)) // Output: false

  /**
   * **Lifting a Partial Function**
   * Converts a PartialFunction into a total function: `Int => Option[Int]`.
   */
  val lifted = aPartialFunction.lift
  println(lifted(2))   // Output: Some(56)
  println(lifted(200)) // Output: None

  /**
   * **Chaining Partial Functions**
   * Add a fallback case using `orElse`.
   */
  val pfChain = aPartialFunction.orElse[Int, Int] {
    case 200 => 67
  }
  println(pfChain(2))   // Output: 56
  println(pfChain(200)) // Output: 67

  /**
   * **Partial Function and Total Function**
   * Partial functions extend total functions.
   */
  val aTotalFunction: Int => Int = {
    case 1 => 99
  }

  /**
   * **Partial Function with Higher-Order Functions**
   * Higher-order functions like `map` accept partial functions.
   */
  val myList = List(1, 2, 3).map {
    case 1 => 100
    case 2 => 200
    case 3 => 300
  }
  println(myList) // Output: List(100, 200, 300)

  /**
   * **Exercise 1: Manual Partial Function**
   * Implementing a partial function manually.
   */
  val aManualFussyFunction = new PartialFunction[Int, Int] {
    override def apply(v1: Int): Int = v1 match {
      case 1 => 42
      case 2 => 56
      case 5 => 999
    }

    override def isDefinedAt(x: Int): Boolean = x == 1 || x == 2 || x == 5
  }

  /**
   * **Exercise 2: Dumb Chatbot**
   * A simple chatbot implemented as a partial function.
   */
  val chatbot: PartialFunction[String, String] = {
    case "hello" => "Hi! How can I assist you?"
    case "bye" => "Goodbye! Have a nice day!"
    case "Scala" => "Scala is a powerful programming language."
  }

  // Using the chatbot
  scala.io.Source.stdin.getLines().foreach { line =>
    println(chatbot.applyOrElse(line, (_: String) => "I don't understand that."))
  }
}
/**
 Key Concepts:
 Partial Function: Only defined for a subset of inputs. Provides a method isDefinedAt to check validity.
 Utilities:
 lift: Converts a partial function to a total function returning Option.
 orElse: Chains another partial function.
 Use in Higher-Order Functions: Partial functions can be used directly in functions like map.
 Exercises:
 Manual Partial Function: Demonstrates how to define a partial function using its trait.
 Dumb Chatbot: A practical application of partial functions to simulate a simple interactive program.
*/