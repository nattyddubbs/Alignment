package com.alignment.store;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class EmailLinkStore {

	private static final Logger logger = Logger.getLogger(EmailLinkStore.class
			.getName());

	private enum EntityProps {
		// The time that a link was hit.
		HIT_TIME,
		// The redirect path for a specific link id.
		REDIRECT;

	}

	private enum KeyNames {
		URL_HIT, REDIRECT;
	}

	public void addUrlHit(String id) {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Url with id " + id + " hit.");
		}

		Key key = KeyFactory.createKey(KeyNames.URL_HIT.name(), id);

		Entity entity = new Entity(id);
		entity.setProperty(EntityProps.HIT_TIME.name(), new Date());

		putInCache(key, entity);
		putInDatastore(entity);
	}

	public void createUrlRedirect(String id, String redirect) {

		Key key = KeyFactory.createKey(KeyNames.REDIRECT.name(), id);

		Entity entity = new Entity(id, key);
		entity.setProperty(EntityProps.REDIRECT.name(), redirect);

		putInCache(key, entity);
		putInDatastore(entity);
	}

	public String getRedirect(String id) throws EntityNotFoundException {
		Key key = KeyFactory.createKey(KeyNames.REDIRECT.name(), id);

		Entity entity = getFromCache(key);

		if (entity != null) {
			return parseRedirect(entity);
		} else {
			entity = getFromDatastore(key);
			return parseRedirect(entity);
		}
	}

	private String parseRedirect(Entity entity) {
		Object redirect = entity.getProperty(EntityProps.REDIRECT.name());
		if (redirect != null) {
			return (String) redirect;
		}
		return null;
	}

	// Helper method to put the provided entity in the datastore.
	private void putInDatastore(Entity entity) {
		AsyncDatastoreService datastore = DatastoreServiceFactory
				.getAsyncDatastoreService();

		datastore.put(entity);
	}

	// Helper method to put the provided entity into the cache.
	private void putInCache(Key key, Entity entity) {
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.put(key, entity);
	}

	// Helper method for getting an entity with the provided key from the
	// datastore.
	private Entity getFromDatastore(Key key) throws EntityNotFoundException {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		return datastore.get(key);
	}

	// Helper method for getting an entity from the cache with the provided key.
	private Entity getFromCache(Key key) {
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		Object value = syncCache.get(key);
		if (value != null && value instanceof Entity) {
			return (Entity) value;
		}
		return null;
	}
}
