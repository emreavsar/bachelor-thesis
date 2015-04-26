package dao;

import org.hibernate.service.spi.InjectService;
import play.Logger;
import play.db.jpa.JPA;

import javax.annotation.Nullable;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Created by emre on 06/03/15.
 */
public abstract class AbstractDAO<T> {
    @NotNull
    private final Class<T> entity;
    @NotNull
    private final String queryReadAll;
    @NotNull
    private final String queryDeleteAll;

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected AbstractDAO() {
        Logger.info("A new dao was created! This should be done with dependency injection!");

        Class<? extends AbstractDAO> clazz = getClass();
        while (!(clazz.getGenericSuperclass() instanceof ParameterizedType)) {
            //noinspection RedundantCast
            clazz = (Class<? extends AbstractDAO>) clazz.getSuperclass();
        }
        ParameterizedType parameterizedType = (ParameterizedType) clazz.getGenericSuperclass();
        entity = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        queryReadAll = "select t from " + entity.getName() + " t ";
        queryDeleteAll = "delete from " + entity.getName();
    }

    public T readById(Long id) {
        return JPA.em().find(entity, id);
    }

    public List<T> readAll() {
        return findAll(queryReadAll);
    }

    public T persist(T entity) {
        JPA.em().persist(entity);
        return entity;
    }

    public void remove(T entity) {
        JPA.em().remove(JPA.em().contains(entity) ? entity : JPA.em().merge(entity));
    }

    public void flush() {
        JPA.em().flush();
    }

    public int removeAll() {
        return JPA.em().createQuery(queryDeleteAll).executeUpdate();
    }

    protected List<T> findAll(String query) {
        return getResultList(JPA.em().createQuery(query));
    }

    protected List<T> findAll(String query, @NotNull Object... params) {
        Query q = JPA.em().createQuery(query);
        for (int i = 1; i <= params.length; i++) {
            q.setParameter(i, params[i - 1]);
        }
        return getResultList(q);
    }

    @SuppressWarnings("unchecked")
    private List<T> getResultList(@NotNull Query q) {
        return q.getResultList();
    }

    @Nullable
    protected T find(String query, Object... params) {
        final List<T> results = findAll(query, params);
        return results.isEmpty() ? null : results.get(0);
    }

}
