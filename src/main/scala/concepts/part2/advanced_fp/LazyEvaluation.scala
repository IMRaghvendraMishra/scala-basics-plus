package concepts.part2.advanced_fp

/**
  * ** Lazy Evaluation in Scala **
  *
  * This object demonstrates the powerful concept of lazy evaluation, highlighting its utility
  * in optimizing performance, reducing unnecessary computations, and enabling infinite data structures.
  *
  * ## Key Concepts:
  *
    * ### 1. ** Lazy Val **:
 *    -Delays the evaluation of a value until it 's accessed for the first time.
    * - Example:
        lazy val x: Int = {
          * println ("hello")
            * 42
          *
        }
          * println(x) // Prints "hello" and "42"
          * println(x) // Prints only "42"
* - Ensures side effects happen only once, even if accessed multiple times.
  *
  * ### 2. ** Lazy Evaluation with Conditions **:
  * - Used in conjunction with logical operators to minimize computation:
*
    lazy val lazyCondition = sideEffectCondition
     println(if (simpleCondition && lazyCondition) "yes" else "no")
* - The second condition(`lazyCondition`) is not evaluated
if the first condition is `false`.
  *
  * ### 3. ** Lazy Evaluation with Call - by - Name Parameters **:
  * -Call - by - name(`=>`) parameters are evaluated lazily each time they are accessed .
* - Combining call -by - name with `lazy` results in ** call - by - need ** , where the parameter
 * is evaluated at most once :
*
def byNameMethod(n: => Int): Int = {
  *
  lazy val t = n // evaluated once
    * t + t + t + 1
  *
}
  * println(byNameMethod(retrieveMagicValue))
  *
  * ### 4. ** Lazy Filters with `withFilter` **:
  * - Filters applied lazily under the hood :
* val numbers = List(1, 25, 40, 5, 23)
*
val lt30lazy = numbers.withFilter(lessThan30) // lazy evaluation
*
val gt20lazy = lt30lazy.withFilter(greaterThan20)
  * gt20lazy.foreach(println) // elements are processed one at a time
* - Used implicitly in ** for - comprehensions ** for efficiency.
  *
  * ### 5. ** Exercise: Lazy Streams **:
  * - Implementing an infinite, lazily evaluated stream:
  * - A `MyStream` can represent an infinite series of values, computed only as needed .
* - Methods include :
  * - `map`, `flatMap`, and `filter` for transformations.
  * - `take` to extract a finite subset of elements .
* - Example:
*
val naturals = MyStream.from(1)(x => x + 1)
  * naturals.take(100).foreach(println) // first 100 natural numbers
  * naturals.map(_ * 2) // all even numbers
*
  * ## Benefits :
  * - Reduces computation overhead by evaluating values only when required.
    * - Enables infinite data structures like streams .
* - Avoids side effects unless necessary.
  *
  * ## To -Do:
  * - Implement the `MyStream` class with all required methods: - `map`, `flatMap`, `filter`, `take`,
 * and `takeAsList`.
* - Provide the `MyStream.from` factory for creating streams with a generator function.
  */
object LazyEvaluation extends App {

  // lazy DELAYS the evaluation of values
  lazy val x: Int = {
    println("hello")
    42
  }
  println(x)
  println(x)

  // examples of implications:
  // side effects
  def sideEffectCondition: Boolean = {
    println("Boo")
    true
  }
  def simpleCondition: Boolean = false

  lazy val lazyCondition = sideEffectCondition
  println(if (simpleCondition && lazyCondition) "yes" else "no")

  // in conjunction with call by name
  def byNameMethod(n: => Int): Int = {
    // CALL BY NEED
    lazy val t = n // only evaluated once
    t + t + t + 1
  }
  def retrieveMagicValue = {
    // side effect or a long computation
    println("waiting")
    Thread.sleep(1000)
    42
  }

  println(byNameMethod(retrieveMagicValue))
  // use lazy vals

  // filtering with lazy vals
  def lessThan30(i: Int): Boolean = {
    println(s"$i is less than 30?")
    i < 30
  }

  def greaterThan20(i: Int): Boolean = {
    println(s"$i is greater than 20?")
    i > 20
  }

  val numbers = List(1, 25, 40, 5, 23)
  val lt30 = numbers.filter(lessThan30) // List(1, 25, 5, 23)
  val gt20 = lt30.filter(greaterThan20)
  println(gt20)

  val lt30lazy = numbers.withFilter(lessThan30) // lazy vals under the hood
  val gt20lazy = lt30lazy.withFilter(greaterThan20)
  println
  gt20lazy.foreach(println)

  // for-comprehensions use withFilter with guards
  for {
    a <- List(1,2,3) if a % 2 == 0 // use lazy vals!
  } yield a + 1
  List(1,2,3).withFilter(_ % 2 == 0).map(_ + 1) // List[Int]

  /*
    Exercise: implement a lazily evaluated, singly linked STREAM of elements.

    naturals = MyStream.from(1)(x => x + 1) = stream of natural numbers (potentially infinite!)
    naturals.take(100).foreach(println) // lazily evaluated stream of the first 100 naturals (finite stream)
    naturals.foreach(println) // will crash - infinite!
    naturals.map(_ * 2) // stream of all even numbers (potentially infinite)
   */
  abstract class MyStream[+A] {
    def isEmpty: Boolean
    def head: A
    def tail: MyStream[A]

    def #::[B >: A](element: B): MyStream[B]  // prepend operator
    def ++[B >: A](anotherStream: MyStream[B]): MyStream[B] // concatenate two streams

    def foreach(f: A => Unit): Unit
    def map[B](f: A => B): MyStream[B]
    def flatMap[B](f: A => MyStream[B]): MyStream[B]
    def filter(predicate: A => Boolean): MyStream[A]

    def take(n: Int): MyStream[A] // takes the first n elements out of this stream
    def takeAsList(n: Int): List[A]
  }

  object MyStream {
    def from[A](start: A)(generator: A => A): MyStream[A] = ???
  }
}
