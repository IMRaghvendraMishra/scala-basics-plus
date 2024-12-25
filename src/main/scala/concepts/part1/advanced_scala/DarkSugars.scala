package concepts.part1.advanced_scala

import scala.util.Try

object DarkSugars extends App {

  // Syntax Sugar #1: method with single param
  def singleArgMethod(arg: Int): String = s"$arg is an int"

  val description = singleArgMethod {
    // Write some complex code
    42
  }

  val aTryInstance_v1 = Try(throw new RuntimeException())
  val aTryInstance_v2 = Try { // similar to Java try { .... }
    throw new RuntimeException()
  }
  val aTryInstance_v3 = Try: // similar to Java try { .... }
    throw new RuntimeException()

  List(1, 2, 3).map(x => x + 1)
  // Same as
  List(1, 2, 3).map {
    x => x + 1
  }
  // Same as
  List(1, 2, 3).map:
    x => x + 1

  // Syntax Sugar #2: Single abstract method
  trait Action {
    def act(x: Int): Int
  }
  //Same as
  trait Action_V2:
    def act(x: Int): Int

  val anActionInstance_v1: Action = new Action {
    def act(x: Int): Int = x + 1
  }
  val anActionInstance_v2: Action = new Action:
    def act(x: Int): Int = x + 1

  val anActionInstance_v3: Action = (x: Int) => x + 1
  val anActionInstance_v4: Action = _ + 1

  // Example: Java Runnable
  val aThread_v1 = new Thread(new Runnable {
      override def run(): Unit = println("Hello Scala")
    }
  )
  val aThread_v2 = new Thread(() => println("Hello Scala from v2"))

  abstract class AnAbstractType {
    def implemented: Int = 23
    def unimplemented(arg: Int): Unit
  }

  val anAbstractTypeInstance: AnAbstractType = (arg: Int) => println(s"$arg is the input")

  // Syntax Sugar #3: :: and #:: methods are special
  val prependedList = 2 :: List(3,4) // List(2, 3, 4)
  // 2.::(List(3,4))
  // List(3,4).::(2)
  println(prependedList)

  // scala spec: last char decides associativity of method
  val prependedList_v2 = 1 :: 2 :: 3 :: List(4,5) // List(1, 2, 3, 4, 5)
  val prependedList_v3 = List(4,5).::(3).::(2).::(1) // List(1, 2, 3, 4, 5)
  println(prependedList_v2)
  println(prependedList_v3)

  class MyStream[T] {
    def -->:(value: T): MyStream[T] = this
  }

  val myStream: MyStream[Int] = 1 -->: 2 -->: 3 -->: new MyStream[Int]

  // Syntax Sugar #4: Multi-word method naming
  class TeenGirl(name: String) {
    def `and then said`(gossip: String): Unit = println(s"$name said $gossip")
  }

  val lilly = new TeenGirl("Lilly")
  lilly `and then said` "Scala is so sweet!"

  // Syntax Sugar #5: infix types
  class Composit[A, B]
  val composit_v1: Composit[Int, String] = new Composit
  val composit_v2: Int Composit String = new Composit // Same as above

  class -->[A, B]

  val towards: Int --> String = new -->

  // syntax sugar #6: update() is very special, much like apply()
  val anArray = Array(1,2,3)
  anArray(2) = 7  // rewritten to anArray.update(2, 7)
  // used in mutable collections
  // remember apply() AND update()!

  // Syntax Sugar #7: setters for mutable containers
  class Mutable {
    private var internalMember: Int = 0
    def member = internalMember
    def member_=(value: Int) = {
      internalMember = value
    }
  }

  val aMutableContainer = new Mutable
  println(aMutableContainer.member)
  aMutableContainer.member = 45 // syntax sugar for aMutableContainer.member_=(45)
  println(aMutableContainer.member)
}
