package caz

import scalaz._
import Scalaz._

object ScalazApp extends App {

  case class Foo(name: String)

  case class Bar(age: Int) 

  println("****************")
  println("Show")
  println("****************")
  println(1.show)
  println("some string".show)

  implicit val fooShow = new Show[Foo]() {
    override def show(foo: Foo) = foo.name
  }

  println(Foo("something").show)

  implicit val barShow = Show.showFromToString[Bar]

  println(Bar(21).show)

  println("****************")
  println("Equal")
  println("****************")

  println(1 === 2)
  println(1 === 1)
  println(1 =/= 2)
  // println(1 === "1") // will not compile

  implicit val barEq = new Equal[Bar]() {
    def equal(f1: Bar, f2: Bar) = f1.age === f2.age
  }

  println(Bar(12) === Bar(21))

  println("****************")
  println("Functor")
  println("****************")

  def sendMessage[F[_]](users: F[String])(implicit functor: Functor[F]) = {
    // functor.map(users)(s => s + "!")
    users.map(s => s + "!")
  }

  println(sendMessage(List("a", "b")))
  println(sendMessage(Option("a")))

  val oa = 1.some
  val ob = 2.some

  val oab = oa |@| ob

  val add: (Int, Int) => Int = (a: Int, b: Int) => a + b

  println(oab.apply(add))

}
