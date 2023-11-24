package de.khamrakulov.followermaze.event.transformer

import java.util.concurrent.{Executors, LinkedBlockingQueue, TimeUnit}

import de.khamrakulov.followermaze._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
class ToTypedEventTransformerSpec extends AnyFlatSpec with Matchers {
  "ToTypedEventTransformer" should "correctly transform events" in {
    val in = new LinkedBlockingQueue[UntypedEvent]()
    in.add(UntypedEvent("1|F|2|3"))
    in.add(UntypedEvent("2|U|2|3"))
    in.add(UntypedEvent("3|B"))
    in.add(UntypedEvent("4|P|2|3"))
    in.add(UntypedEvent("5|S|2"))

    val out = new LinkedBlockingQueue[TypedEvent]()
    var running = () => true
    val service = Executors.newSingleThreadExecutor()
    service.execute(EventTransformer.toTypedEvent(in, out, running))

    out.take() shouldBe Follow(1, 2, 3)
    out.take() shouldBe Unfollow(2, 2, 3)
    out.take() shouldBe Broadcast(3)
    out.take() shouldBe PrivateMsg(4, 2, 3)
    out.take() shouldBe StatusUpdate(5, 2)
    running = () => false
    service.shutdown()
    service.awaitTermination(3, TimeUnit.SECONDS)
  }
}
