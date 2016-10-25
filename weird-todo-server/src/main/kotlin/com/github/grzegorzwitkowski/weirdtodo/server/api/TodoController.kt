package com.github.grzegorzwitkowski.weirdtodo.server.api

import com.github.grzegorzwitkowski.weirdtodo.server.JmsPriority
import com.github.grzegorzwitkowski.weirdtodo.server.days
import com.github.grzegorzwitkowski.weirdtodo.server.timeToLive
import org.joda.time.DateTime
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RestController
import java.io.Serializable
import javax.jms.DeliveryMode
import javax.jms.MessageProducer
import javax.jms.ObjectMessage
import javax.jms.Queue
import javax.jms.Session

@RestController
@RequestMapping(consumes = arrayOf(APPLICATION_JSON_VALUE), produces = arrayOf(APPLICATION_JSON_VALUE))
class TodoController {

    companion object {
        val log = LoggerFactory.getLogger(TodoController::class.java)
    }

    private val jmsSession: Session
    private val queue: Queue
    private val messageProducer: MessageProducer

    @Autowired
    constructor(jmsSession: Session, @Qualifier("newTodosQueue") queue: Queue) {
        this.jmsSession = jmsSession
        this.queue = queue
        this.messageProducer = jmsSession.createProducer(queue)
    }

    @RequestMapping(path = arrayOf("/todo"), method = arrayOf(POST))
    fun saveTodo(@RequestBody todo: Todo) {
        val message: ObjectMessage = jmsSession.createObjectMessage(todo)
        messageProducer.send(message, DeliveryMode.PERSISTENT, JmsPriority.NORMAL, timeToLive(3.days()))
        log.info("""new todo was sent to queue "${queue.queueName}": $todo""")

    }
}

data class Todo(val content: String, val description: String, val dateCreated: DateTime) : Serializable
