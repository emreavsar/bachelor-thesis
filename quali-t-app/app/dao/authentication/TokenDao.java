package dao.authentication;

import dao.AbstractDAO;
import exceptions.EntityNotFoundException;
import models.authentication.Token;

/**
 * Created by emre on 31/03/15.
 */
public class TokenDao extends AbstractDAO<Token> {

    public Token findByToken(String token) throws EntityNotFoundException {
        return find("select t from Token t where t.token=?", token);
    }
}
