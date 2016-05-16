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

  val len: String => Int = str => str.length()
  val maybeName: Option[String] = "name".some

  // use function designed for wrapped type
  println(maybeName.map(len))

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

  println("****************")
  println("Apply")
  println("****************")

  // from functor
  println(Functor[Option].map(maybeName)(len))
  println(maybeName.map(len))

  val maybeLen: Option[String => Int] = len.some
  println(Apply[Option].ap(maybeName)(maybeLen))

  println("maybeName <*> maybeLen: " + (maybeName <*> maybeLen))

  // apply two argument function
  val isLong: (String, Int) => Boolean = (str, bound) => len(str) > bound
  val maybeBoundry: Option[Int] = 10.some

  val temp: String => (Int => Boolean) = isLong.curried
  val temp2: Option[Int => Boolean] = maybeName.map(isLong.curried)

  println(maybeBoundry <*> maybeName.map(isLong.curried))

}
