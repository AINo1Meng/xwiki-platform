/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.notifications.filters.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.component.manager.ComponentManager;
import org.xwiki.model.ModelContext;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.model.reference.WikiReference;
import org.xwiki.notifications.NotificationException;
import org.xwiki.notifications.filters.NotificationFilter;
import org.xwiki.notifications.filters.NotificationFilterProvider;
import org.xwiki.notifications.preferences.NotificationPreference;
import org.xwiki.wiki.descriptor.WikiDescriptorManager;

/**
 * This is the default implementation of {@link NotificationFilterProvider}. This component is used in order to gather
 * notification filters that are stored as XObjects in the user profile.
 *
 * @version $Id$
 * @since 9.7RC1
 */
@Component
@Singleton
@Named(UserProfileNotificationFilterProvider.NAME)
public class UserProfileNotificationFilterProvider implements NotificationFilterProvider
{
    /**
     * The name of the provider.
     */
    public static final String NAME = "userProfile";

    private static final String ERROR_MESSAGE = "Failed to get all the notification filters.";

    @Inject
    private ComponentManager componentManager;

    @Inject
    private WikiDescriptorManager wikiDescriptorManager;

    @Inject
    private ModelContext modelContext;

    @Override
    public int getProviderPriority()
    {
        return 500;
    }

    @Override
    public Set<NotificationFilter> getAllFilters(DocumentReference user) throws NotificationException
    {
        // If the user is from the main wiki, get filters from all wikis
        if (user.getWikiReference().getName().equals(wikiDescriptorManager.getMainWikiId())) {

            String currentWikiId = wikiDescriptorManager.getCurrentWikiId();

            Map<String, NotificationFilter> filters = new HashMap<>();
            try {
                for (String wikiId : wikiDescriptorManager.getAllIds()) {
                    modelContext.setCurrentEntityReference(new WikiReference(wikiId));

                    filters.putAll(componentManager.getInstanceMap(NotificationFilter.class));
                }
            } catch (Exception e) {
                throw new NotificationException(ERROR_MESSAGE, e);
            } finally {
                modelContext.setCurrentEntityReference(new WikiReference(currentWikiId));
            }

            return new HashSet<>(filters.values());
        } else {
            // If the user is local, get filters from the current wiki only (we assume it's the wiki of the user).
            try {
                return new HashSet<>(componentManager.getInstanceList(NotificationFilter.class));
            }  catch (Exception e) {
                throw new NotificationException(ERROR_MESSAGE, e);
            }
        }
    }

    @Override
    public Set<NotificationFilter> getFilters(DocumentReference user, NotificationPreference preference)
            throws NotificationException
    {
        Set<NotificationFilter> filters = getAllFilters(user);

        Iterator<NotificationFilter> it = filters.iterator();

        while (it.hasNext()) {
            NotificationFilter filter = it.next();

            if (!filter.matchesPreference(preference)) {
                it.remove();
            }
        }

        return filters;
    }

    @Override
    public void saveFilters(Collection<NotificationFilter> filters) throws NotificationException
    {
        throw new NotificationException("Unable to save the given filters. The operation is not supported by the "
                + "UserProfileNotificationFilterProvider");
    }
}
