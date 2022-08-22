package org.theeuropeanlibrary.repox.rest.servlets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.OPTIONS;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.theeuropeanlibrary.repox.rest.pathOptions.Result;
import org.theeuropeanlibrary.repox.rest.pathOptions.StatisticsOptionListContainer;

import pt.utl.ist.configuration.ConfigSingleton;
import pt.utl.ist.configuration.DefaultRepoxContextUtil;
import pt.utl.ist.statistics.RepoxStatistics;
import pt.utl.ist.statistics.StatisticsManager;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * Statistics context path handling.
 * 
 * @author Simon Tzanakis (Simon.Tzanakis@theeuropeanlibrary.org)
 * @since Dec 8, 2014
 */
@Path("/" + StatisticsOptionListContainer.STATISTICS)
@Api(value = "/" + StatisticsOptionListContainer.STATISTICS, description = "Rest api for statistics context")
public class StatisticsResource {
	@Context
	UriInfo uriInfo;

	public StatisticsManager statisticsManager;

	/**
	 * Initialize fields before serving.
	 */
	public StatisticsResource() {
		ConfigSingleton.setRepoxContextUtil(new DefaultRepoxContextUtil());
		statisticsManager = ConfigSingleton.getRepoxContextUtil()
				.getRepoxManager().getStatisticsManager();
	}

	/**
	 * Creates a new instance by providing the DataManager. (For Tests)
	 * @param statisticsManager 
	 */
	public StatisticsResource(StatisticsManager statisticsManager) {
		super();
		this.statisticsManager = statisticsManager;
	}

	/**
	 * Retrieve all the available options for Statistics. Relative path :
	 * /statistics
	 * 
	 * @return the list of the options available wrapped in a container
	 */
	@OPTIONS
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ApiOperation(value = "Get options over statistics conext.", httpMethod = "OPTIONS", response = StatisticsOptionListContainer.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK (Response containing a list of all available options)") })
	public StatisticsOptionListContainer getOptions() {
		StatisticsOptionListContainer statisticsOptionListContainer = new StatisticsOptionListContainer(
				uriInfo.getBaseUri());
		return statisticsOptionListContainer;
	}

	/**
	 * Retrieve all the available options for Statistics. Relative path :
	 * /statistics/options
	 * 
	 * @return the list of the options available wrapped in a container
	 */
	@GET
	@Path("/" + StatisticsOptionListContainer.OPTIONS)
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ApiOperation(value = "Get options over statistics conext.", httpMethod = "GET", response = StatisticsOptionListContainer.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK (Response containing a list of all available options)") })
	public StatisticsOptionListContainer getGETOptions() {
		StatisticsOptionListContainer statisticsOptionListContainer = new StatisticsOptionListContainer(
				uriInfo.getBaseUri());
		return statisticsOptionListContainer;
	}

	/**
	 * Retrieve the statistics. 
	 * Relative path : /statistics
	 * 
	 * @return the list of the options available wrapped in a container
	 * @throws InternalServerErrorException 
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ApiOperation(value = "Retrieve the statistics.", httpMethod = "GET", response = String.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK (Response containing a String message)"),
			@ApiResponse(code = 500, message = "InternalServerErrorException")
			})
	public Response getStatistics() throws InternalServerErrorException{
		RepoxStatistics statistics;
		Document statisticsReport;
		try {
			statistics = statisticsManager.generateStatistics(null);
			statisticsReport = statisticsManager
					.getStatisticsReport(statistics);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			if (statisticsReport != null) {
				OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
						baos, "UTF-8");
				OutputFormat format = OutputFormat.createPrettyPrint();
				XMLWriter writer = new XMLWriter(outputStreamWriter, format);
				writer.write(statisticsReport);
				writer.close();
			}
			return Response.status(200).entity(new Result(baos.toString("UTF-8"))).build();
		} catch (IOException | DocumentException | SQLException e) {
			throw new InternalServerErrorException("Internal Server Error : " + e.getMessage());
		}
	}
}
