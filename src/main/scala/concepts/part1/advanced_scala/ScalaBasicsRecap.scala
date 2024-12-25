package concepts.part1.advanced_scala

import scala.annotation.tailrec

/**
 * This Scala object is a comprehensive **recap of Scala basics**, covering key concepts
 * in both object-oriented and functional programming. It's designed to provide a quick
 * reference for essential Scala features and syntax, helping beginners and intermediates
 * solidify their understanding. Key topics include:
 *
 * 1. **Conditionals and Expressions**:
 *    - Differentiates between instructions (imperative) and expressions (functional).
 *    - Example: `if` expressions return values.
 *
 * 2. **Type Inference and Unit**:
 *    - Scala infers types automatically.
 *    - `Unit` is equivalent to `void` in other languages and signifies "no meaningful value".
 *
 * 3. **Functions and Recursion**:
 *    - Functions are first-class citizens.
 *    - Demonstrates tail recursion for efficient memory usage.
 *
 * 4. **Object-Oriented Programming**:
 *    - Classes, inheritance, and traits (interfaces).
 *    - Subtyping polymorphism and method notations for natural language syntax.
 *    - Example: `aCroc eat aDog`.
 *
 * 5. **Anonymous Classes and Generics**:
 *    - Create instances of traits or classes on-the-fly using anonymous classes.
 *    - Introduces generics for parameterized types, e.g., `MyList[+A]`.
 *
 * 6. **Case Classes**:
 *    - Immutable data structures with built-in equality and pattern matching support.
 *
 * 7. **Exceptions**:
 *    - Handling exceptions with `try`, `catch`, and `finally`.
 *    - Example: Graceful handling of runtime exceptions.
 *
 * 8. **Functional Programming**:
 *    - Higher-order functions (HOFs) like `map`, `flatMap`, and `filter`.
 *    - Anonymous functions (lambdas) for concise logic representation.
 *
 * 9. **For-Comprehensions**:
 *    - A functional syntax for iterating over collections with optional filters.
 *    - Example: Produces Cartesian products of numbers and characters.
 *
 * 10. **Collections and Data Structures**:
 *     - Core types like `Seq`, `List`, `Map`, and `Tuple`.
 *     - Special collections like `Option` and `Try` for safer programming.
 *
 * 11. **Pattern Matching**:
 *     - Matches values and deconstructs objects.
 *     - Examples include simple value matching and case class decomposition.
 *
 * This file encapsulates foundational Scala concepts and idiomatic practices, making it
 * a go-to resource for reviewing the language's basics.
 */
object ScalaBasicsRecap extends App {

  val aCondition: Boolean = false
  val aConditionedVal = if (aCondition) 42 else 65
  // instructions vs expressions

  // compiler infers types for us
  val aCodeBlock = {
    if (aCondition) 54
    56
  }

  // Unit = void
  val theUnit = println("hello, Scala")

  // functions
  def aFunction(x: Int): Int = x + 1

  // recursion: stack and tail
  @tailrec def factorial(n: Int, accumulator: Int): Int =
    if (n <= 0) accumulator
    else factorial(n - 1, n * accumulator)

  // object-oriented programming

  class Animal
  class Dog extends Animal
  val aDog: Animal = new Dog // subtyping polymorphism

  trait Carnivore {
    def eat(a: Animal): Unit
  }

  class Crocodile extends Animal with Carnivore {
    override def eat(a: Animal): Unit = println("crunch!")
  }

  // method notations
  val aCroc = new Crocodile
  aCroc.eat(aDog)
  aCroc eat aDog // natural language

  // anonymous classes
  val aCarnivore = new Carnivore {
    override def eat(a: Animal): Unit = println("roar!")
  }

  // generics
  abstract class MyList[+A] // variance and variance problems in THIS course
  // singletons and companions
  object MyList

  // case classes
  case class Person(name: String, age: Int)

  // exceptions and try/catch/finally

  // val throwsException = throw new RuntimeException  // Nothing
  val aPotentialFailure = try {
    throw new RuntimeException
  } catch {
    case e: Exception => "I caught an exception"
  } finally {
    println("some logs")
  }

  // packaging and imports

  // functional programming
  val incrementer = new Function1[Int, Int] {
    override def apply(v1: Int): Int = v1 + 1
  }

  incrementer(1)

  val anonymousIncrementer = (x: Int) => x + 1
  List(1,2,3).map(anonymousIncrementer) // HOF
  // map, flatMap, filter

  // for-comprehension
  val pairs = for {
    num <- List(1,2,3) // if condition
    char <- List('a', 'b', 'c')
  } yield num + "-" + char

  // Scala collections: Seqs, Arrays, Lists, Vectors, Maps, Tuples
  val aMap = Map(
    "Daniel" -> 789,
    "Jess" -> 555
  )

  // "collections": Options, Try
  val anOption = Some(2)

  // pattern matching
  val x = 2
  val order = x match {
    case 1 => "first"
    case 2 => "second"
    case 3 => "third"
    case _ => x + "th"
  }

  val bob = Person("Bob", 22)
  val greeting = bob match {
    case Person(n, _) => s"Hi, my name is $n"
  }

  // all the patterns
}