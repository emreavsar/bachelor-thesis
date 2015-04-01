package dao.authentication;

import dao.AbstractDAO;
import models.authentication.Token;
import models.authentication.User;

/**
 * Created by emre on 31/03/15.
 */
public class TokenDao extends AbstractDAO<Token> {

    public Token findByToken(String token) {
        return find("select t from Token t where t.token=?", token);
    }
}
