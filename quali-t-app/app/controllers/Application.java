package controllers;

import dao.authentication.RoleDao;
import dao.models.*;
import models.Customer;
import models.Nfr;
import models.Project;
import models.QualityAttribute;
import models.authentication.Role;
import models.authentication.User;
import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.libs.Yaml;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.*;


public class Application extends Controller {

    public static Result index() {
        return redirect("/app/index.html");
    }

    @Transactional
    public static Result getNFR(Long id) {
        NfrDao nfrDao = new NfrDao();
        Nfr firstNfr = nfrDao.readById(new Long(id));

        // TODO (emre): avoid checking for null somehow
        if (firstNfr != null) {
            return ok(Json.toJson(firstNfr));
        } else {
            return notFound();
        }
    }

    // Creates two projects with all 5 SMART qa's with customer and return json
    @Transactional
    public static Result setCustomerProject(String customername) {
        Customer customer = new Customer(customername);
        Set<QualityAttribute> q_set = new HashSet<QualityAttribute>();
        QualityAttribute q1 = new QualityAttribute("S", "Specified");
        QualityAttribute q2 = new QualityAttribute("M", "Measruable");
        QualityAttribute q3 = new QualityAttribute("A", "Achievable");
        QualityAttribute q4 = new QualityAttribute("R", "Realistic");
        QualityAttribute q5 = new QualityAttribute("T", "Timeable");
        QualityAttributeDAO qualityAttributeDAO = new QualityAttributeDAO();
        qualityAttributeDAO.persist(q1);
        qualityAttributeDAO.persist(q2);
        qualityAttributeDAO.persist(q3);
        qualityAttributeDAO.persist(q4);
        qualityAttributeDAO.persist(q5);
        q_set.add(q1);
        q_set.add(q2);
        q_set.add(q3);
        q_set.add(q4);
        q_set.add(q5);
        Project project = new Project("projekt3", customer, q_set);
        Project project2 = new Project("projekt4", customer, q_set);
        q1.addUsedByProject(project);
        q2.addUsedByProject(project);
        q3.addUsedByProject(project);
        q4.addUsedByProject(project);
        q5.addUsedByProject(project);
        q1.addUsedByProject(project2);
        q2.addUsedByProject(project2);
        q3.addUsedByProject(project2);
        q4.addUsedByProject(project2);
        q5.addUsedByProject(project2);
        customer.addProject(project);
        customer.addProject(project2);
        ProjectDAO projectDAO = new ProjectDAO();
        projectDAO.persist(project);
        projectDAO.persist(project2);
        CustomerDAO customerDAO = new CustomerDAO();
        Customer customer2 = customerDAO.persist(customer);
        return ok(Json.toJson(customerDAO.readById(customer2.getId())));
    }
}
