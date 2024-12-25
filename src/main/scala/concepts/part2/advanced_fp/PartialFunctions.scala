package concepts.part2.advanced_fp

object PartialFunctions extends App {

  class FunctionNotApplicableException extends RuntimeException

  val aFunction = (x: Int) => x + 1 // Function[Int, Int] === Int => Int

  val aFussyFunction = (x: Int) => {
    if(x == 1) 42
    else if(x == 2) 56
    else if(x == 5) 999
    else throw new FunctionNotApplicableException
  }

  // Same as above using pattern matching
  val aNicerFussyFunction = (x: Int) => x match {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  }
  // {1,2,5} => Int

  // below is equivalent  of above
  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  } // partial function value

  println(aPartialFunction(2))
  // println(aPartialFunction.apply(2)) // Same as above
  // println(aPartialFunction(12345)) // Crash with MatchError

  // Partial Function utilities
  println(aPartialFunction.isDefinedAt(2))
  println(aPartialFunction.isDefinedAt(200))

  // lift
  val lifted = aPartialFunction.lift // Int => Option[Int]
  println(lifted(2)) // Some(2)
  println(lifted(200)) // None

  // default value
  val pfChain = aPartialFunction.orElse[Int, Int] {
    case 200 => 67
  }
  println(pfChain(2))
  println(pfChain(200))

  // partial function extends normal function
  val aTotalFunction: Int => Int = {
    case 1 => 99
  }

  // Higher order function accept partial function as well
  val myList = List(1,2,3).map {
    case 1 => 100
    case 2 => 200
    case 3 => 300
    // case 30 => 300 // scala.MatchError: 3 (of class java.lang.Integer) -> if list value do not find a matching case
  }

  println(myList)

  /**
   * Partial Function can have only one parameter type
   *
   * Exercise
   *
   * 1. Construct a partial function instance by instantiating the partial function trait (anonymous class)
   * 2. Implement a dumb chatbot as a partial function
   */

  val aManualFussyFunction = new PartialFunction[Int, Int] {
    override def apply(v1: Int): Int = ???

    override def isDefinedAt(x: Int): Boolean = ???

  }


  scala.io.Source.stdin.getLines().foreach(line => println(s"you said $line"))



}
