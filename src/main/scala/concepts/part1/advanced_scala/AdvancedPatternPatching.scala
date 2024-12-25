package concepts.part1.advanced_scala

/**
 * This Scala object demonstrates advanced concepts of **pattern matching**,
 * which is a powerful feature for handling and decomposing data structures.
 * The file covers several key aspects of pattern matching, including:
 *
 * 1. **Matching on Lists**: Demonstrates pattern matching on list structures.
 * 2. **Custom Extractors**:
 *    - Using `unapply` and `unapplySeq` methods to define custom pattern matching logic.
 *    - Shows how to decompose custom classes and return specific data.
 *      3. **Special Matching Techniques**:
 *    - Matching with infix patterns.
 *    - Decomposing sequences.
 *      4. **Custom Return Types for Extractors**:
 *    - Shows how to define extractors that use custom return types instead of `Option`.
 *      5. **Exercises**: Includes examples like single-digit and double-digit matchers.
 *
 * Key Concepts:
 * - **Extractors**: Classes or objects that define an `unapply` method to enable pattern matching.
 * - **Infix Patterns**: Allows matching two inputs using an operator-like syntax (e.g., `a Or b`).
 * - **Recursive Matching**: Decomposes recursive structures such as custom lists using `unapplySeq`.
 *
 * This file is designed for developers exploring intermediate to advanced Scala features,
 * providing examples that highlight both practical use cases and nuanced techniques of pattern matching.
 */
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

  // Infix paterns -> ::
  // Infix pattern make sense when we have 2 inputs only
  case class Or[A, B](a: A, b: B) // In Scala it is called Either
  val either = Or(2, "Two")
  val humanDescription = either match {
    // case Or(num, str) => s"$num is written as $str"
    case num Or str => s"$num is written as $str" // same as above
  }
  println(humanDescription)

  // decomposing sequences
  val vararg = numbers match {
    case List(1, _*) => "starting with 1"
  }

  abstract class MyList[+A] {
    def head: A = ???
    def tail: MyList[A] = ???
  }
  case object MyEmptyList extends MyList[Nothing]
  case class MyNonEmptyList[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]

  object MyList {
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] =
      if (list == MyEmptyList) Some(Seq.empty)
      else unapplySeq(list.tail).map(list.head +: _)
  }

  val myList: MyList[Int] = MyNonEmptyList(1, MyNonEmptyList(2, MyNonEmptyList(3, MyNonEmptyList(4, MyNonEmptyList(5, MyEmptyList)))))
  val decomposed = myList match {
    case MyList(1, 2, 3, _*) => s"Starting with 1, 2, 3"
    case _ => s"Default myList"
  }
  println(decomposed)

  // The return types of unapply and unapplySeq is not necessarily be an Option

  // custom return type for unapply
  // isEmpty: Boolean, get: Something
  abstract class Wrapper[T] {
    def isEmpty: Boolean
    def get: T
  }

  object PersonWrapper {
    def unapply(person: Person): Wrapper[String] = new Wrapper[String] {
      def isEmpty: Boolean = false
      def get: String = person.name
    }
  }

  println(mohit match {
    case PersonWrapper(n) => s"This person's name is $n"
    case _ => s"Default person"
  })

}
