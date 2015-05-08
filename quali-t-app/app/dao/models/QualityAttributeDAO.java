package dao.models;

import dao.AbstractDAO;
import exceptions.EntityNotFoundException;
import models.template.QA;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by corina on 31.03.2015.
 */
public class QualityAttributeDAO extends AbstractDAO<QA> {
    public List<QA> findAllById(List<Long> qaIds) throws EntityNotFoundException {
        List<QA> qas = new ArrayList();
        for (Long qa : qaIds) {
            qas.add(readById(qa));
        }
        return qas;
    }

    public ArrayList<QA> search(String searchArgument) {
        ArrayList<QA> searchResults = new ArrayList<>();
        searchArgument = "%" + searchArgument + "%";
        searchResults = (ArrayList<QA>) findAll("select q from QA q where lower(q.description) like ?", searchArgument);
        return searchResults;
    }

    public List<QA> readAllLatest() {
        return findAll("select q from QA q where q.isDeleted=false");
    }
}
