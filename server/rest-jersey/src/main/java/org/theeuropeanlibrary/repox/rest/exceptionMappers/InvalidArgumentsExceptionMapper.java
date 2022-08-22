/* InvalidValueExceptionHandler.java - created on Oct 10, 2014, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.repox.rest.exceptionMappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import org.theeuropeanlibrary.repox.rest.pathOptions.Result;

import pt.utl.ist.util.exceptions.InvalidArgumentsException;

/**
 * Exception handler for the {@link pt.utl.ist.util.exceptions.InvalidArgumentsException}
 * 
 * @author Simon Tzanakis (Simon.Tzanakis@theeuropeanlibrary.org)
 * @since Oct 10, 2014
 */
@Provider
public class InvalidArgumentsExceptionMapper implements ExceptionMapper<InvalidArgumentsException> {
    @Override
    public Response toResponse(InvalidArgumentsException ex) {
        //Status: 400, Info: Bad Request
        return Response.status(400).entity(new Result(ex.getMessage())).build();
    }
}
