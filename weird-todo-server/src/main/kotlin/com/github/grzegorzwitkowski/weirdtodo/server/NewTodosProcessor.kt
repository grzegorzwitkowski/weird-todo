package com.github.grzegorzwitkowski.weirdtodo.server

import com.github.grzegorzwitkowski.weirdtodo.server.api.Todo
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.jms.MessageConsumer
import javax.jms.ObjectMessage
import javax.jms.Queue
import javax.jms.Session

@Component
class NewTodosProcessor {

    companion object {
        val log = LoggerFactory.getLogger(NewTodosProcessor::class.java)
    }

    private val queue: Queue

    @Autowired
    constructor(jmsSession: Session, queue: Queue) {
        this.queue = queue
        val newTodosMessageConsumer: MessageConsumer = jmsSession.createConsumer(queue)
        newTodosMessageConsumer.setMessageListener { message ->
            val todo: Todo = (message as ObjectMessage).getObject() as Todo
            processNewTodo(todo)
        }
    }

    private fun processNewTodo(todo: Todo) {
        log.info("""received todo from queue "${queue.queueName}": $todo""")
    }
}
