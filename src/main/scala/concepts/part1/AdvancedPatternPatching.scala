package concepts.part1

object AdvancedPatternPatching extends App {

  val numbers = List(1)
  val description = numbers match {
    case head :: Nil => println(s"The only element is $head")
    case _ => println("anything")
  }

  /**
   * Available structures for pattern matching are:
   * - constants
   * - wildcards
   * - case classes
   * - tuples
   * - some special magic like above
   */

  class Person(val name: String, val age: Int)
  object Person {
    def unapply(person: Person): Option[(String, Int)] = {
      if(person.age < 18) None
      else Some((person.name, person.age))
    }

    def unapply(age: Int): Some[String]= {
      Some(if(age < 18) "Not eligible" else "Eligible")
    }
  }

  val mohit = new Person("Mohit", 20)
  val greeting = mohit match {
    case Person(n, a) => s"Hi, my name is $n, I'm $a, and I'm eligible for voting" // it will not compile without unapply method
    case _ => s"I'm not eligible for voting"
  }

  println(greeting)

  val votingStatus = mohit.age match {
    case Person(status) => s"My voting status is : $status"
  }

  println(votingStatus)

  // Exercise
  ////////////////////////
  object singleDigit {
    def unapply(arg: Int): Boolean = arg > -10 && arg < 10
  }

  object doubleDigit {
    def unapply(arg: Int): Boolean = arg > -100 && arg < 100
  }
  //////////////////////////

  val n = 12
  val matchProperty = n match {
    case singleDigit() => "single digit"
    case doubleDigit() => "double digit"
    case _ => "no match"
  }
  println(matchProperty)

}
