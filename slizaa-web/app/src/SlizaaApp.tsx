/*
 * slizaa-web - Slizaa Static Software Analysis Tools
 * Copyright © 2019 Code-Kontor GmbH and others (slizaa@codekontor.io)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
import './SlizaaApp.css';

import { InMemoryCache } from 'apollo-cache-inmemory';
import ApolloClient from 'apollo-client';
import { createHttpLink } from 'apollo-link-http';
import * as React from "react";
import { Component } from "react";
import { ApolloProvider } from 'react-apollo';
import { Provider } from 'react-redux';
import { createStore } from 'redux';
import { composeWithDevTools } from 'redux-devtools-extension';
import { appReducer } from './redux/Reducers';
import MainView from './views/mainview/internal/MainView';
import { ServerConfigValidator } from './views/serverconfigwizard';

// TODO: origin url
const httpLink = createHttpLink({
  uri: 'http://localhost:8085/graphql/'
});

// create the apollo client instance
const client = new ApolloClient({
  cache: new InMemoryCache(),
  link: httpLink,
});

const store = createStore(appReducer, composeWithDevTools());

class SlizaaApp extends Component {

  public render() {
    return (
      <ApolloProvider client={client}>
        <Provider store={store}>
          <ServerConfigValidator>
            <MainView />
          </ServerConfigValidator>  
        </Provider>
      </ApolloProvider>
    );
  }
}

export default SlizaaApp;
