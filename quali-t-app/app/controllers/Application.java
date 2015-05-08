package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import dao.models.CatalogQADAO;
import dao.models.QualityAttributeDAO;
import exceptions.EntityNotFoundException;
import models.template.CatalogQA;
import models.template.QA;
import models.template.QAVar;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Application extends Controller {
    public static Result index() {
        return redirect("/app/index.html");
    }

    @Restrict({@Group("dashboard")})
    @Transactional
    public static Result getFirstQA() {
        QualityAttributeDAO qualityAttributeDAO = new QualityAttributeDAO();
        QA qa = null;
        try {
            qa = qualityAttributeDAO.readById(5000L);
            return ok(Json.toJson(qa));
        } catch (EntityNotFoundException e) {
            return notFound(e.getMessage());
        }
    }

    // Creates two projects with all 5 SMART qa's with customer and return json
    @Transactional
    public static Result setCustomerProject(String customername) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("1", "Text1");
        parameters.put("2", "Text2");
//        QAVar qaVar = createVariable(1, "enumText", parameters);
//        QAVar qaVar2 = createVariable(1, "enumNumber", parameters);
        parameters.clear();
        parameters.put("1", "5");
        parameters.put("2", "10");
        parameters.put("min", "0");
        parameters.put("max", "15");
        parameters.put("default", "5");
//        QAVar qaVar2 = createVariable(1, "enumNumber", parameters);
        List<QAVar> list = new ArrayList<>();
//        list.add(qaVar);
//        list.add(qaVar2);
//        new QAVarValDAO().persist();
        CatalogQA qa = new CatalogQA();
        qa.addVars(list);
        new CatalogQADAO().persist(qa);


//        QA qa = new QA("Der Use Case Einkaufsabwicklung muss mit weniger als 5 Klicks durchfuehrbar sein.");
//        models.template.Catalog cat = new Catalog(customername, "", "", "");
//        qa.addUsedInCatalog(cat);
//        cat.addTemplate(qa);
//        QualityAttributeDAO qadao = new QualityAttributeDAO();
//        qadao.persist(qa);
//        Customer customer = new Customer(customername, "adress");
//        Set<QualityProperty> q_set = new HashSet<QualityProperty>();
//        QualityProperty q1 = new QualityProperty("S", "Specified");
//        QualityProperty q2 = new QualityProperty("M", "Measruable");
//        QualityProperty q3 = new QualityProperty("A", "Achievable");
//        QualityProperty q4 = new QualityProperty("R", "Realistic");
//        QualityProperty q5 = new QualityProperty("T", "Timeable");
//        QualityAttributeDAO qualityAttributeDAO = new QualityAttributeDAO();
//        qualityAttributeDAO.persist(q1);
//        qualityAttributeDAO.persist(q2);
//        qualityAttributeDAO.persist(q3);
//        qualityAttributeDAO.persist(q4);
//        qualityAttributeDAO.persist(q5);
//        q_set.add(q1);
//        q_set.add(q2);
//        q_set.add(q3);
//        q_set.add(q4);
//        q_set.add(q5);
//        Project project = new Project("projekt3", customer);
//        Project project2 = new Project("projekt4", customer, q_set);
//        q1.addUsedByProject(project);
//        q2.addUsedByProject(project);
//        q3.addUsedByProject(project);
//        q4.addUsedByProject(project);
//        q5.addUsedByProject(project);
//        q1.addUsedByProject(project2);
//        q2.addUsedByProject(project2);
//        q3.addUsedByProject(project2);
//        q4.addUsedByProject(project2);
//        q5.addUsedByProject(project2);
//        customer.addProject(project);
//        customer.addProject(project2);
//        ProjectDAO projectDAO = new ProjectDAO();
//        projectDAO.persist(project);
//        projectDAO.persist(project2);
//        CustomerDAO customerDAO = new CustomerDAO();
//        Customer customer2 = customerDAO.persist(customer);
//        return ok(Json.toJson(customerDAO.readById(customer2.getId())));
          return ok(Json.toJson(qa));
//        return ok();
    }
}
