package concepts.part2.advanced_fp

/**
 * **Monads in Scala**
 *
 * This object explores the concept of monads, their implementation, and their significance in functional programming.
 *
 * ## Key Concepts:
 *
 * ### 1. **Monad Definition**:
 * A Monad is an abstraction that represents computations defined as a sequence of steps.
 * - Requires two primary operations:
 *   1. **unit** (or `apply`): Wraps a value into the monad context.
 *      2. **flatMap**: Chains operations while preserving the monad context.
 *
 * ### 2. **Custom Monad: `Attempt`**:
 * - Models computations that may fail (similar to `Try`).
 * - Encapsulates successful and failed computations:
 * trait Attempt[+A] {
 * def flatMap[B](f: A => Attempt[B]): Attempt[B]
 * }
 * - Concrete implementations:
 *   - `Success`: Represents successful computations.
 *   - `Fail`: Represents failed computations.
 *     - Example usage:
 *       val attempt = Attempt {
 *       throw new RuntimeException("My own monad, yes!")
 *       }
 *
 * ### 3. **Monad Laws**:
 * Monads must satisfy the following properties:
 * - **Left Identity**:
 * unit.flatMap(f) == f(x)
 * Attempt(x).flatMap(f) == f(x)
 * - **Right Identity**:
 * attempt.flatMap(unit) == attempt
 * - **Associativity**:
 * attempt.flatMap(f).flatMap(g) == attempt.flatMap(x => f(x).flatMap(g))
 *
 * ### 4. **Exercise: `Lazy[T]` Monad**:
 * - Encapsulates a computation that will only be executed when accessed.
 * - Combines lazy evaluation with monadic chaining:
 * class Lazy[+A](value: => A) {
 * private lazy val internalValue = value
 * def use: A = internalValue
 * def flatMap[B](f: (=> A) => Lazy[B]): Lazy[B] = f(internalValue)
 * }
 * object Lazy {
 * def apply[A](value: => A): Lazy[A] = new Lazy(value) // unit
 * }
 * - Example:
 * val lazyInstance = Lazy {
 * println("Today I don't feel like doing anything")
 * 42
 * }
 * val flatMappedInstance = lazyInstance.flatMap(x => Lazy {
 * 10 * x
 * })
 * flatMappedInstance.use
 *
 * ### 5. **Monadic Operations: `map` and `flatten`**:
 * - `map` transforms the values in a monad:
 * def map[B](f: T => B): Monad[B] = flatMap(x => unit(f(x)))
 * - `flatten` collapses nested monads:
 * def flatten[T](m: Monad[Monad[T]]): Monad[T] = m.flatMap(x => x)
 * - Example with `List`:
 * List(1,2,3).map(_ * 2) == List(1,2,3).flatMap(x => List(x * 2))
 * List(List(1, 2), List(3, 4)).flatten == List(1,2,3,4)
 *
 * ## Practical Applications:
 * - **Error Handling**: Using monads like `Try` or `Attempt` to manage exceptions gracefully.
 * - **Lazy Computations**: Leveraging `Lazy` to optimize performance and avoid unnecessary computations.
 * - **Chaining Operations**: Seamlessly composing transformations while abstracting the underlying context.
 */
object Monads extends App {

  // our own Try monad

  trait Attempt[+A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B]
  }
  object Attempt {
    def apply[A](a: => A): Attempt[A] =
      try Success(a) catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Success[+A](value: A) extends Attempt[A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B] =
      try f(value) catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Fail(e: Throwable) extends Attempt[Nothing] {
    def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this
  }

  /*
    left-identity

    unit.flatMap(f) = f(x)
    Attempt(x).flatMap(f) = f(x) // Success case!
    Success(x).flatMap(f) = f(x) // proved.

    right-identity

    attempt.flatMap(unit) = attempt
    Success(x).flatMap(x => Attempt(x)) = Attempt(x) = Success(x)
    Fail(e).flatMap(...) = Fail(e)

    associativity

    attempt.flatMap(f).flatMap(g) == attempt.flatMap(x => f(x).flatMap(g))
    Fail(e).flatMap(f).flatMap(g) = Fail(e)
    Fail(e).flatMap(x => f(x).flatMap(g)) = Fail(e)

    Success(v).flatMap(f).flatMap(g) =
      f(v).flatMap(g) OR Fail(e)

    Success(v).flatMap(x =>  f(x).flatMap(g)) =
      f(v).flatMap(g) OR Fail(e)

   */

  val attempt = Attempt {
    throw new RuntimeException("My own monad, yes!")
  }

  println(attempt)

  /*
    EXERCISE:
    1) implement a Lazy[T] monad = computation which will only be executed when it's needed.
      unit/apply
      flatMap

    2) Monads = unit + flatMap
       Monads = unit + map + flatten

       Monad[T] {

        def flatMap[B](f: T => Monad[B]): Monad[B] = ... (implemented)

        def map[B](f: T => B): Monad[B] = ???
        def flatten(m: Monad[Monad[T]]): Monad[T] = ???

        (have List in mind)
   */

  // 1 - Lazy monad
  class Lazy[+A](value: => A) {
    // call by need
    private lazy val internalValue = value
    def use: A = internalValue
    def flatMap[B](f: (=> A) => Lazy[B]): Lazy[B] = f(internalValue)
  }
  object Lazy {
    def apply[A](value: => A) = new Lazy(value) // unit
  }

  val lazyInstance = Lazy {
    println("Today I don't feel like doing anything")
    42
  }

  val flatMappedInstance = lazyInstance.flatMap(x => Lazy {
    10 * x
  })
  val flatMappedInstance2 = lazyInstance.flatMap(x => Lazy {
    10 * x
  })
  flatMappedInstance.use
  flatMappedInstance2.use

  /*
    left-identity
    unit.flatMap(f) = f(v)
    Lazy(v).flatMap(f) = f(v)

    right-identity
    l.flatMap(unit) = l
    Lazy(v).flatMap(x => Lazy(x)) = Lazy(v)

    associativity: l.flatMap(f).flatMap(g) = l.flatMap(x => f(x).flatMap(g))

    Lazy(v).flatMap(f).flatMap(g) = f(v).flatMap(g)
    Lazy(v).flatMap(x => f(x).flatMap(g)) = f(v).flatMap(g)
   */

  def flatten[T](lz: Lazy[Lazy[T]]) = lz.flatMap(x => x)
  // 2: map and flatten in terms  of flatMap
  /*
    Monad[T] { // List
      def flatMap[B](f: T => Monad[B]): Monad[B] = ... (implemented)

      def map[B](f: T => B): Monad[B] = flatMap(x => unit(f(x))) // Monad[B]
      def flatten(m: Monad[Monad[T]]): Monad[T] = m.flatMap((x: Monad[T]) => x)

      List(1,2,3).map(_ * 2) = List(1,2,3).flatMap(x => List(x * 2))
      List(List(1, 2), List(3, 4)).flatten = List(List(1, 2), List(3, 4)).flatMap(x => x) = List(1,2,3,4)
    }

   */
}
