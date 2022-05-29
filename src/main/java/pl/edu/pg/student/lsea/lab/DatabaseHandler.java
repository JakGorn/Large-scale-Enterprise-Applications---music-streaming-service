package pl.edu.pg.student.lsea.lab;

import pl.edu.pg.student.lsea.lab.artist.Artist;
import pl.edu.pg.student.lsea.lab.song.Song;
import pl.edu.pg.student.lsea.lab.user.User;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Class which enables easy communication by CRUD operations with database
 * @author Piotr Cichacki
*/
public class DatabaseHandler<T> {
    /** entity manager */
    private final EntityManager em;
    /** entity class */
    private final Class<T> entityClass;

    /**
     * Creates new database handler with given parameters
     * @param em entity manager
     * @param entityClass class of the entity
     */
    public DatabaseHandler(EntityManager em, Class<T> entityClass) {
        this.em = em;
        this.entityClass = entityClass;
    }

    /**
     * Receiving data from the database
     * @param type name of the data type to receive, available options: "song", "artist", "user"
     * @param name identifier of the data to receive, "all" to receive all data of given type
     * @return list of the object founded in the database, "null" for unavailable data type
     */
	@SuppressWarnings("unchecked")
	public List<T> get_from_database(String type, String name) {
        List<T> queryResult = null;
        Query query;

        switch(type) {
            case "song":
                if (name.equals("all")) {
                    query = this.em.createNamedQuery("findAllSongs", Song.class);
                } else {
                    query = this.em.createNamedQuery("findSong_byName", Song.class);
                    query.setParameter("name", name);
                }
                queryResult = query.getResultList();
                break;
            case "artist":
                if (name.equals("all")) {
                    query = this.em.createNamedQuery("findAllArtists", Artist.class);
                } else {
                    query = this.em.createNamedQuery("findArtist_byStageName", Artist.class);
                    query.setParameter("stageName", name);
                }
                queryResult = query.getResultList();
                break;
            case "user":
                if (name.equals("all")) {
                    query = this.em.createNamedQuery("findAllUsers", User.class);
                } else {
                    query = this.em.createNamedQuery("findUser_byUsername", User.class);
                    query.setParameter("username", name);
                }
                queryResult = query.getResultList();
                break;
        }
        return queryResult;
    }

    /**
     * Saving data to the database
     * @param entity object to be saved to the database
     */
    public void save_to_database(T entity) {
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
    }

    /**
     * Removing data from the database
     * @param entity object to be removed from the database
     */
    public void remove_from_database(T entity) {
        em.getTransaction().begin();
        em.remove(entity);
        em.getTransaction().commit();
    }

    /**
     * Updating data in the database
     * @param type name of the data type to receive, available options: "artist", "user"
     * @param info array of fields values to be updated in the object
     * @param obj object to be updated in the database
     */
    public void update_to_database(String type, String[] info, Object obj) {
        switch(type) {
            case "artist":
                Artist artist = (Artist) obj;
                em.getTransaction().begin();
                artist.setStageName(info[1]);
                artist.setCountry(info[2]);
                artist.setGenre(info[3]);
                em.getTransaction().commit();
                em.clear();
                break;
            case "user":
                User user = (User) obj;
                em.getTransaction().begin();
                user.setUsername(info[1]);
                user.setCountry(info[2]);
                em.getTransaction().commit();
                this.em.clear();
                break;
            default:
                break;
        }

    }

}
