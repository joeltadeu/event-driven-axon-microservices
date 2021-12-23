package com.eventdrivenmicroservices.productservice.command.interceptor;

import com.eventdrivenmicroservices.productservice.command.CreateProductCommand;
import com.eventdrivenmicroservices.productservice.core.persistence.entity.ProductLookupEntity;
import com.eventdrivenmicroservices.productservice.core.persistence.repository.ProductLookupRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiFunction;

@Component
@Slf4j
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private final ProductLookupRepository repository;

    public CreateProductCommandInterceptor(
        ProductLookupRepository repository) {
        this.repository = repository;
    }

    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
        List<? extends CommandMessage<?>> list) {
        return (index, command) -> {

            log.info("Intercepted command: " + command.getPayloadType());

            if (CreateProductCommand.class.equals(command.getPayloadType())) {

                CreateProductCommand createProductCommand = (CreateProductCommand) command.getPayload();

                ProductLookupEntity entity = repository.findByProductIdOrTitle(createProductCommand.getProductId(),
                    createProductCommand.getTitle());

                if (entity != null) {
                    throw new IllegalStateException(String.format("Product with productId %s or title %s already " +
                                                                  "exist", createProductCommand.getProductId(),
                        createProductCommand.getTitle()));
                }
            }
            return command;
        };
    }
}
