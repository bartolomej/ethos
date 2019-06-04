package scripts;

import db.DbFacade;

public class Reset {

    public static void main(String[] args) {
        DbFacade.purgeStore();
    }
}
