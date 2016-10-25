package com.github.grzegorzwitkowski.weirdtodo.server.api

import com.fasterxml.jackson.annotation.JsonCreator
import org.joda.time.DateTime
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RestController
import java.io.Serializable
import javax.jms.MessageProducer
import javax.jms.ObjectMessage
import javax.jms.Session

@RestController
@RequestMapping(consumes = arrayOf(APPLICATION_JSON_VALUE), produces = arrayOf(APPLICATION_JSON_VALUE))
class TodoController(private val jmsSession: Session) {

    private val newTodosDestination: MessageProducer = jmsSession.createProducer(jmsSession.createQueue("new-todos"))

    @RequestMapping(path = arrayOf("/todo"), method = arrayOf(POST))
    fun saveTodo(@RequestBody todo: Todo) {
        val message: ObjectMessage? = jmsSession.createObjectMessage(todo)
        newTodosDestination.send(message)
    }
}

data class Todo(val content: String, val description: String, val dateCreated: DateTime) : Serializable