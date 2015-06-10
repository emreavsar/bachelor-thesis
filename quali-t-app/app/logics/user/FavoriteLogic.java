
package logics.user;

import com.google.inject.Inject;
import dao.models.ProjectDAO;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import logics.authentication.Authenticator;
import models.authentication.User;
import models.project.Project;

import java.util.Map;

/**
 * Created by emre on 25/04/15.
 */
public class FavoriteLogic {

    @Inject
    private Authenticator authenticator;
    @Inject
    private ProjectDAO projectDAO;


    public User setFavoriteProject(Long userId, Map<String, Object> favoriteProject) throws MissingParameterException, EntityNotFoundException {
        if (userId != null && favoriteProject != null) {
            User u;
            Project projectToFavorite = projectDAO.readById((Long) favoriteProject.get("projectToFavorite"));
            // add favorite
            if ((boolean) favoriteProject.get("isFavorite")) {
                u = authenticator.getUser(userId).addToFavorites(projectToFavorite);
            } else { // remove favorite
                u = authenticator.getUser(userId).removeFromFavorites(projectToFavorite);
            }
            return authenticator.update(u);
        }
        throw new MissingParameterException("Please provide all required parameters!");
    }
}
