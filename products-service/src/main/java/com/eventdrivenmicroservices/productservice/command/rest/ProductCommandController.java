package com.eventdrivenmicroservices.productservice.command.rest;

import com.eventdrivenmicroservices.productservice.command.CreateProductCommand;
import com.eventdrivenmicroservices.productservice.command.rest.dto.CreateProductDto;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductCommandController {

    private final CommandGateway commandGateway;

    ProductCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public String createProduct(@Valid
                                @RequestBody CreateProductDto createProductDto) {
        CreateProductCommand command = CreateProductCommand.builder()
            .price(createProductDto.getPrice())
            .quantity(createProductDto.getQuantity())
            .title(createProductDto.getTitle())
            .productId(UUID.randomUUID().toString())
            .build();

        return commandGateway.sendAndWait(command);
    }
}
