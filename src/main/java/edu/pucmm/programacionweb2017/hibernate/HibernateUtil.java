/*
 * Copyright 2017 Gruas Romano S.R.L
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.pucmm.programacionweb2017.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gusta on 12/22/2016.
 */
public class HibernateUtil {
    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
    public static SessionFactory sessionFactory = buildSessionFactory();
    public static Session session = null;

    /**
     * Funcion que contruye la sesion de hibernate para la interaccion con la base de datos
     *
     * @return
     */
    public static SessionFactory buildSessionFactory() {
        SessionFactory sessionFactory = null;
        try {
            Configuration configuration = new Configuration();
            configuration.configure("/public/hibernate.cfg.xml");
            sessionFactory = configuration.buildSessionFactory();
        } catch (ServiceException e) {
            logger.debug("No esta conectado a internet. Excepcion -> ", e);
        }
        return sessionFactory;
    }

    /**
     * Funcion que me devuelve la sessionfactory inicializada
     *
     * @return
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session getSession() {
        return session;
    }

    /**
     * Funcion que me devuelve una nueva sesion abierta
     *
     * @return
     */
    public static Session openSession() {
        return getSessionFactory().openSession();
    }

    /**
     * Funcion que cierra la sesion abierta
     *
     * @return
     */
    public static void closeSession() {
        session.close();
    }
}
