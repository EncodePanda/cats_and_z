package caz

// import all
// import cats.implicits._

object CatsApp extends App {

  case class Foo(name: String)

  case class Bar(age: Int)

  println("****************")
  println("Show")
  println("****************")

  import cats.syntax.show._
  import cats.Show
  import cats.std.int._
  import cats.std.string._
  import cats.syntax.option._

  println(1.show)
  println("some string".show)

  implicit val fooShow = new Show[Foo] {
    override def show(foo: Foo) = foo.name
  }

  println(Foo("something").show)

  implicit val barShow = Show.fromToString[Bar]

  println(Bar(21).show)

  println("****************")
  println("Equal")
  println("****************")

  import cats.syntax.eq._
  import cats.Eq

  println(1 === 2)
  println(1 === 1)
  println(1 =!= 2)

  // println(false) //c will not compile
  // println(1 === "1") // will not compile

  implicit val barEq = new Eq[Bar]() {
    def eqv(f1: Bar, f2: Bar) = f1.age === f2.age
  }

  println(Bar(12) === Bar(21))

  println("****************")
  println("Functor")
  println("****************")

  import cats.syntax.functor._
  import cats.std.list._
  import cats.std.option._
  import cats.Functor

  val len: String => Int = str => str.length()
  val maybeName: Option[String] = "name".some

  // use function designed for wrapped type
  println(maybeName.map(len))

  // implet fucntions with abstracted context
  def sendMessage[F[_]](users: F[String])(implicit functor: Functor[F]) = {
    // functor.map(users)(s => s + "!")
    users.map(s => s + "!")
  }

  println(sendMessage(List("a", "b")))
  println(sendMessage(Option("a")))

  case class Baz[T](name: T)

  implicit val bazFunctor = new Functor[Baz] {
    override def map[A, B](fa: Baz[A])(f: A => B): Baz[B] = Baz(f(fa.name))
  }

  println(sendMessage(Baz("reksio")))

  // https://gist.github.com/larsrh/5500542

  println("****************")
  println("Apply")
  println("****************")

  import cats.syntax.apply._
  import cats.Apply

  // from functor
  println(Functor[Option].map(maybeName)(len))
  println(maybeName.map(len))

  val maybeLen: Option[String => Int] = len.some
  println(Apply[Option].ap(maybeLen)(maybeName))

  maybeLen.ap(maybeName)

  // apply two argument function
  val isLong: (String, Int) => Boolean = (str, bound) => len(str) > bound
  val maybeBoundry: Option[Int] = 10.some

  val temp: String => (Int => Boolean) = isLong.curried
  val temp2: Option[Int => Boolean] = maybeName.map(isLong.curried)

  println(maybeName.map(isLong.curried).ap(maybeBoundry))
  println(isLong.some.ap2(maybeName, maybeBoundry))

  // sth later

  println("****************")
  println("Applicative Builder (scream operator)")
  println("****************")

  import cats.syntax.cartesian._

  val dbConf = "db config".some // could be NONE, becuse parer thornw excpetion
  val restConf = "rest config".some
  val globalConf = "global conf".some

  val run: (String, String) => String = (db, rest) => db + rest

  run.some.ap2(dbConf, restConf)

  (dbConf |@| restConf |@| globalConf).map {
    case (db, rest, global) =>  db + rest +  global
  }

  println((maybeName |@| maybeBoundry).map(isLong))

}
