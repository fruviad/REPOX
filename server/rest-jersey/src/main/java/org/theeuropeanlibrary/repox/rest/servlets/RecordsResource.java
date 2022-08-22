/* RecordsResource.java - created on Dec 5, 2014, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.repox.rest.servlets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.OPTIONS;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.theeuropeanlibrary.repox.rest.pathOptions.RecordOptionListContainer;
import org.theeuropeanlibrary.repox.rest.pathOptions.Result;

import pt.utl.ist.configuration.ConfigSingleton;
import pt.utl.ist.configuration.DefaultRepoxContextUtil;
import pt.utl.ist.dataProvider.DefaultDataManager;
import pt.utl.ist.util.InvalidInputException;
import pt.utl.ist.util.Urn;
import pt.utl.ist.util.exceptions.DoesNotExistException;
import pt.utl.ist.util.exceptions.InvalidArgumentsException;
import pt.utl.ist.util.exceptions.MissingArgumentsException;
import pt.utl.ist.util.exceptions.ObjectNotFoundException;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * Records context path handling.
 * 
 * @author Simon Tzanakis (Simon.Tzanakis@theeuropeanlibrary.org)
 * @since Dec 5, 2014
 */
@Path("/" + RecordOptionListContainer.RECORDS)
@Api(value = "/" + RecordOptionListContainer.RECORDS, description = "Rest api for records context")
public class RecordsResource {
    @Context
    UriInfo                   uriInfo;

    public DefaultDataManager dataManager;
    public Urn                urn;        //For mocking tests

    /**
     * Initialize fields before serving.
     */
    public RecordsResource() {
        ConfigSingleton.setRepoxContextUtil(new DefaultRepoxContextUtil());
        dataManager = ((DefaultDataManager)ConfigSingleton.getRepoxContextUtil().getRepoxManager().getDataManager());
    }

    /**
     * Creates a new instance by providing the DataManager. (For Tests)
     * @param dataManager
     * @param urn 
     */
    public RecordsResource(DefaultDataManager dataManager, Urn urn) {
        super();
        this.dataManager = dataManager;
        this.urn = urn;
    }

    /**
     * Retrieve all the available options for Records.
     * Relative path : /records
     * @return the list of the options available wrapped in a container
     */
    @OPTIONS
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @ApiOperation(value = "Get options over record conext.", httpMethod = "OPTIONS", response = RecordOptionListContainer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK (Response containing a list of all available options)")
    })
    public RecordOptionListContainer getOptions() {
        RecordOptionListContainer recordOptionListContainer = new RecordOptionListContainer(uriInfo.getBaseUri());
        return recordOptionListContainer;
    }

    /**
     * Retrieve all the available options for Records.
     * Relative path : /records/options
     * @return the list of the options available wrapped in a container
     */
    @GET
    @Path("/" + RecordOptionListContainer.OPTIONS)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @ApiOperation(value = "Get options over record conext.", httpMethod = "GET", response = RecordOptionListContainer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK (Response containing a list of all available options)")
    })
    public RecordOptionListContainer getGETOptions() {
        return getOptions();
    }

    /**
     * Retrieve the record with the provided id.
     * Relative path : /records
     * @param recordId 
     * @return OK or Error Message 
     * @throws DoesNotExistException 
     * @throws InvalidArgumentsException 
     * @throws IOException 
     * @throws InternalServerErrorException 
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @ApiOperation(value = "Get specific record.", httpMethod = "GET", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK (Response containing a Record)"),
            @ApiResponse(code = 400, message = "InvalidArgumentsException"),
            @ApiResponse(code = 404, message = "DoesNotExistException"),
            @ApiResponse(code = 500, message = "InternalServerErrorException")
    })
    public Response getRecord(@ApiParam(value = "Id of record", required = true) @QueryParam("recordId") String recordId) throws DoesNotExistException, InvalidArgumentsException, IOException,
            InternalServerErrorException {
        Urn recordUrn = null;
        try {
            if (this.urn != null) //For mocking tests
                recordUrn = this.urn;
            else
                recordUrn = new Urn(recordId);
        } catch (InvalidInputException e) {
            throw new InvalidArgumentsException("Invalid argument: " + e.getMessage());
        }

        Node record = null;
        try {
            record = dataManager.getRecord(recordUrn);
        } catch (IOException | DocumentException | SQLException e) {
            throw new InternalServerErrorException("Internal Server Error : " + e.getMessage());
        } catch (ObjectNotFoundException e) {
            throw new DoesNotExistException("Does NOT exist: " + e.getMessage());
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (record != null)
        {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(baos, "UTF-8");
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(outputStreamWriter, format);
            writer.write(record);
            writer.close();
        }
        else
            throw new DoesNotExistException("Does NOT exist: " + "Record with id " + recordId + " NOT found!");

        return Response.status(200).entity(new Result(baos.toString("UTF-8"))).build();
    }

    /**
     * Deletes (mark) or permanently erase a record.
     * Relative path : /records  
     * @param recordId 
     * @param type 
     * @return OK or Error Message
     * @throws DoesNotExistException 
     * @throws MissingArgumentsException 
     * @throws InvalidArgumentsException 
     */
    @DELETE
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @ApiOperation(value = "Deletes (mark) or permanently erase a record.", httpMethod = "DELETE", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK (Response containing a String message)"),
            @ApiResponse(code = 400, message = "InvalidArgumentsException"),
            @ApiResponse(code = 404, message = "DoesNotExistException"),
            @ApiResponse(code = 406, message = "MissingArgumentsException"),
            @ApiResponse(code = 500, message = "InternalServerErrorException")
    })
    public Response removeRecord(
            @ApiParam(value = "Id of record", required = true) @QueryParam("recordId") String recordId,
            @ApiParam(value = "Delete(mark) or erase(permanent)", defaultValue = RecordOptionListContainer.DELETE, allowableValues = RecordOptionListContainer.DELETE + " , " + RecordOptionListContainer.ERASE) @DefaultValue(RecordOptionListContainer.DELETE) @QueryParam("type") String type)
            throws DoesNotExistException,
            MissingArgumentsException, InvalidArgumentsException {

        if (recordId == null || recordId.equals(""))
            throw new MissingArgumentsException("Missing value: " + "recordId type missing!");
        if (type == null || type.equals("") || (!type.equals(RecordOptionListContainer.DELETE) && !type.equals(RecordOptionListContainer.ERASE)))
            throw new MissingArgumentsException("Missing value: " + "Query parameter type not valid!");

        if (type.equals(RecordOptionListContainer.DELETE))
        {
            try {
                dataManager.deleteRecord(recordId);
            } catch (DocumentException | SQLException | IOException e) {
                throw new InternalServerErrorException("Internal Server Error : " + e.getMessage());
            } catch (ObjectNotFoundException e) {
                throw new DoesNotExistException("Does NOT exist: " + e.getMessage());
            } catch (InvalidInputException e) {
                throw new InvalidArgumentsException("Invalid argument: " + e.getMessage());
            }
            return Response.status(200).entity(new Result("Record with id: " + recordId + " deleted!")).build();
        }
        else //Erase
        {
            try {
                dataManager.eraseRecord(recordId);
            } catch (DocumentException | SQLException | IOException e) {
                throw new InternalServerErrorException("Internal Server Error : " + e.getMessage());
            } catch (ObjectNotFoundException e) {
                throw new DoesNotExistException("Does NOT exist: " + e.getMessage());
            } catch (InvalidInputException e) {
                throw new InvalidArgumentsException("Invalid argument: " + e.getMessage());
            }
            return Response.status(200).entity(new Result("Record with id: " + recordId + " erased!")).build();
        }
    }

    /**
     * Create a new record.
     * Relative path : /records  
     * @param datasetId 
     * @param recordId 
     * @param recordString 
     * 
     * @return OK or Error Message
     * @throws DoesNotExistException 
     * @throws MissingArgumentsException 
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Consumes(MediaType.APPLICATION_XML)
    @ApiOperation(value = "Create a new record.", httpMethod = "POST", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "OK (Response containing a String message)"),
            @ApiResponse(code = 404, message = "DoesNotExistException"),
            @ApiResponse(code = 406, message = "MissingArgumentsException"),
            //            @ApiResponse(code = 409, message = "AlreadyExistsException"),
            @ApiResponse(code = 500, message = "InternalServerErrorException")
    })
    public Response createRecord(@ApiParam(value = "Id of dataset", required = true) @QueryParam("datasetId") String datasetId,
            @ApiParam(value = "Id of record", required = true) @QueryParam("recordId") String recordId, @ApiParam(value = "Record data", required = true) String recordString)
            throws DoesNotExistException, MissingArgumentsException {

        if (datasetId == null || datasetId.equals(""))
            throw new MissingArgumentsException("Missing value: " + "datasetId type missing!");
        if (recordId == null || recordId.equals(""))
            throw new MissingArgumentsException("Missing value: " + "recordId type missing!");
        if (recordString == null || recordString.equals(""))
            throw new MissingArgumentsException("Missing value: " + "Record information is empty!");

        try {
            dataManager.saveRecord(recordId, datasetId, recordString);
        } catch (IOException | DocumentException e) {
            throw new InternalServerErrorException("Internal Server Error : " + e.getMessage());
        } catch (ObjectNotFoundException e) {
            throw new DoesNotExistException("Does NOT exist: " + e.getMessage());
        }
        return Response.status(201).entity(new Result("Record with id: " + recordId + " created!")).build();
    }
}
