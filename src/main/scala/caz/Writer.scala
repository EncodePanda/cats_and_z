package caz

import scalaz._, Scalaz._
import scalaz.concurrent.Task

object WriterAndTransformers extends App {

  println("********* Transformers *********")

  def calculateStacked(n: Int): Task[Option[Int]] = Task.now((n+100).some)

  def calculate(n: Int): OptionT[Task, Int] = OptionT(calculateStacked(n))

  val result: OptionT[Task, String] = for {
    n <- calculate(10)
  } yield (s"calculated $n")

  val unwrappedResult: Task[Option[String]] = result.run

  val theInt = 10
  val someInt: Option[Int] = 20.some
  val noneInt: Option[Int] = None
  val taskInt: Task[Int] = Task.now(30)

  val liftIntO: OptionT[Task, Int] = theInt.point[OptionT[Task, ?]]
  val liftSomeO: OptionT[Task, Int] = OptionT(someInt.point[Task])
  val liftNoneO: OptionT[Task, Int] = OptionT(noneInt.point[Task])
  val liftTaskO: OptionT[Task, Int] = OptionT(taskInt.map(_.point[Option]))
  
  val calculate2: Task[Exception \/ String] = Task.delay {
    "something".right[Exception]
  }

  val liftTaskEitherE: EitherT[Task, Exception, String] = EitherT(calculate2)
  val liftEitherE: EitherT[Task, Exception, String] =
    EitherT("string".right[Exception].point[Task])
  val liftTaskE: EitherT[Task, Exception, String] =
    EitherT("something".point[Task].map(_.point[Exception \/ ?]))

  println("********* Sequence & Traverse *********")

  val sth: List[Task[String]] = List(Task.now("cat"), Task.now("mouse"))

  val sthElse: Task[List[String]] = sth.sequence

  // val thereAndBackAgain: Option[Task[String]] = sthElse.sequence

  val yetAnother1: Task[List[String]] = List("a", "b").map(Task.now(_)).sequence
  val yetAnother2: Task[List[String]] = List("a", "b").traverse(Task.now(_))


  // Writer (L, A)


}
