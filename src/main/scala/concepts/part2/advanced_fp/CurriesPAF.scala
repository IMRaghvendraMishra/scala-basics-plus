package concepts.part2.advanced_fp

/**
 * This Scala object dives into **curried functions, partial function applications (PAF)**,
 * and advanced function manipulations, offering a clear exploration of functional programming paradigms.
 * It is a valuable resource for understanding key Scala concepts. Here's a breakdown:
 *
 * 1. **Curried Functions**:
 *    - Functions returning other functions, enabling partial application.
 *    - Example: `val add3 = superAdder(3)` creates a new function to add `3`.
 *    - Demonstrates how curried methods (e.g., `curriedAdder`) differ from standard functions.
 *
 * 2. **ETA-Expansion**:
 *    - Converts methods into functions to satisfy type requirements.
 *    - Example: `val add4: Int => Int = curriedAdder(4)` lifts a method to a function.
 *
 * 3. **Partial Function Applications (PAF)**:
 *    - Using underscores `_` to create partially applied functions.
 *    - Example: `val add5 = curriedAdder(5) _` lifts the method into a function.
 *
 * 4. **Advanced Function Transformations**:
 *    - Multiple ways to create functions like `add7` using methods and lambdas.
 *    - Highlights Scala's flexibility with syntax like `.curried` and `_`.
 *
 * 5. **String Manipulations with Functions**:
 *    - Example: Using placeholders (`_`) to partially apply a function:
 *      - `val insertName = concatenator("Hello, I'm ", _: String, ", how are you?")`.
 *
 * 6. **Exercise: Number Formatting**:
 *    - Implements a curried formatter function to process lists of numbers in various formats (`%4.2f`, etc.).
 *    - Demonstrates lifting methods to functions for concise application over collections.
 *
 * 7. **Methods vs Functions**:
 *    - Explains the distinction and shows how to convert between them using ETA-expansion and lambdas.
 *
 * 8. **By-Name Parameters vs 0-Lambda Parameters**:
 *    - Demonstrates the difference:
 *      - By-name (`=>`) evaluates the argument lazily.
 *      - By-function (`() =>`) requires an explicit function.
 *    - Explores various invocation scenarios, highlighting nuances.
 *
 * This file provides a practical approach to functional programming with Scala, emphasizing
 * reusability, expressiveness, and flexibility in working with functions and methods.
 */
object CurriesPAF extends App {

  // curried functions
  val superAdder: Int => Int => Int =
    x => y => x + y

  val add3 = superAdder(3) // Int => Int = y => 3 + y
  println(add3(5))
  println(superAdder(3)(5)) // curried function

  // METHOD!
  def curriedAdder(x: Int)(y: Int): Int = x + y // curried method

  val add4: Int => Int = curriedAdder(4)
  // lifting = ETA-EXPANSION

  // functions != methods (JVM limitation)
  def inc(x: Int) = x + 1
  List(1,2,3).map(x => inc(x))  // ETA-expansion

  // Partial function applications
  val add5 = curriedAdder(5) _ // Int => Int

  // EXERCISE
  val simpleAddFunction = (x: Int, y: Int) => x + y
  def simpleAddMethod(x: Int, y: Int) = x + y
  def curriedAddMethod(x: Int)(y: Int) = x + y

  // add7: Int => Int = y => 7 + y
  // as many different implementations of add7 using the above
  // be creative!
  val add7 = (x: Int) => simpleAddFunction(7, x)  // simplest
  val add7_2 = simpleAddFunction.curried(7)
  val add7_6 = simpleAddFunction(7, _: Int) // works as well

  val add7_3 = curriedAddMethod(7) _  // PAF
  val add7_4 = curriedAddMethod(7)(_) // PAF = alternative syntax

  val add7_5 = simpleAddMethod(7, _: Int) // alternative syntax for turning methods into function values
  // y => simpleAddMethod(7, y)

  // underscores are powerful
  def concatenator(a: String, b: String, c: String) = a + b + c
  val insertName = concatenator("Hello, I'm ", _: String, ", how are you?") // x: String => concatenator(hello, x, howarewyou)
  println(insertName("Daniel"))

  val fillInTheBlanks = concatenator("Hello, ", _: String, _: String) // (x, y) => concatenator("Hello, ", x, y)
  println(fillInTheBlanks("Daniel", " Scala is awesome!"))

  // EXERCISES
  /*
    1.  Process a list of numbers and return their string representations with different formats
        Use the %4.2f, %8.6f and %14.12f with a curried formatter function.
   */
  def curriedFormatter(s: String)(number: Double): String = s.format(number)
  val numbers = List(Math.PI, Math.E, 1, 9.8, 1.3e-12)

  val simpleFormat = curriedFormatter("%4.2f") _ // lift
  val seriousFormat = curriedFormatter("%8.6f") _
  val preciseFormat = curriedFormatter("%14.12f") _

  println(numbers.map(curriedFormatter("%14.12f"))) // compiler does sweet eta-expansion for us

  /*
    2.  difference between
        - functions vs methods
        - parameters: by-name vs 0-lambda
   */
  def byName(n: => Int) = n + 1
  def byFunction(f: () => Int) = f() + 1

  def method: Int = 42
  def parenMethod(): Int = 42

  /*
    calling byName and byFunction
    - int
    - method
    - parenMethod
    - lambda
    - PAF
   */
  byName(23)  // ok
  byName(method)  // ok
  byName(parenMethod())
  // byName(parenMethod) // Scala 2: ok but beware ==> byName(parenMethod()); Scala 3 forbids calling the method with no parens
  //  byName(() => 42) // not ok
  byName((() => 42)()) // ok
  //  byName(parenMethod _) // not ok

  //  byFunction(45) // not ok
  //  byFunction(method) // not ok!!!!!! does not do ETA-expansion!
  byFunction(parenMethod) // compiler does ETA-expansion
  byFunction(() => 46) // works
  byFunction(parenMethod _) // also works, but warning- unnecessary
}