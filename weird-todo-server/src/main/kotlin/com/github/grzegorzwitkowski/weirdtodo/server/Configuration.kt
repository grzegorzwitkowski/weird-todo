package com.github.grzegorzwitkowski.weirdtodo.server

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.activemq.broker.BrokerService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.annotation.PreDestroy
import javax.jms.Connection
import javax.jms.Queue
import javax.jms.Session

@Configuration
open class Configuration {

    private val connection: Connection

    constructor() {
        val broker: BrokerService = BrokerService()
        broker.addConnector("tcp://localhost:61616")
        broker.start()

        val connectionFactory: ActiveMQConnectionFactory = ActiveMQConnectionFactory("tcp://localhost:61616")
        connectionFactory.trustedPackages = arrayListOf(
                "com.github.grzegorzwitkowski.weirdtodo.server.api",
                "org.joda.time"
        )
        connection = connectionFactory.createConnection()
        connection.start()
    }

    @Bean
    open fun jsmSession(): Session {
        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
    }

    @Bean
    open fun objectMapper(): ObjectMapper {
        return ObjectMapper().registerModules(KotlinModule(), JodaModule())
    }

    @Bean(name = arrayOf("newTodosQueue"))
    open fun newTodosQueue(jmsSession: Session): Queue {
        return jmsSession.createQueue("new-todos")
    }

    @PreDestroy
    fun cleanup() {
        connection.close()
    }
}