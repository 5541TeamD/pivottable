package ca.concordia.pivottable.controller;

import ca.concordia.pivottable.entities.ShareableSchema;
import ca.concordia.pivottable.servicelayer.SchemaManagementService;
import ca.concordia.pivottable.utils.DependenciesContainer;
import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class ImportSchemaController extends Controller {

    SchemaManagementService service;

    /**
     * All controllers need a constructor that needs the container as argument.
     *
     * @param container The DI container
     */
    public ImportSchemaController(DependenciesContainer container) {
        super(container);
        service = container.get("SchemaManagementService");
    }

    @Override
    protected Object handleAction(Request request, Response response) throws Exception {
        String currentUser = request.session().attribute("username");
        request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
        Part uploadedFileData = request.raw().getPart("uploaded_file");
        try (InputStream is = uploadedFileData.getInputStream()) {
            // Use the input stream to create a file
            String json = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));
            ShareableSchema schema = ShareableSchema.fromJSon(json);
            schema.setOwnerUsername(currentUser);
            schema.setschemaID(null);
            String newName = getFileName(uploadedFileData);
            if (newName != null) {
                newName = newName.replace(".json", "");
                schema.setSchemaName(newName);
            }
            Long ret = service.createShareableSchema(schema);
            return successResponse(ret.toString(), response);
        } catch (IOException ioe) {
            log.error("IO Exception... " + ioe.getMessage());
            throw ioe;
        }
    }

    private static String getFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}
