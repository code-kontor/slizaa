/**
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
package io.codekontor.slizaa.server.web;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class SlizaaWebController {

  private HashMap<String, String> mimeTypeMap;

  @PostConstruct
  public void onceConstructed() throws IOException {

    mimeTypeMap = new HashMap<>();
    mimeTypeMap.put("css", "text/css");
    mimeTypeMap.put("html", "text/html");
    mimeTypeMap.put("js", "text/javascript");
    mimeTypeMap.put("svg", "image/svg+xml");
    mimeTypeMap.put("ico", "image/x-icon");
  }

  @RequestMapping(value = { "/index.html" })
  public RedirectView slizaaRootIndex(HttpServletRequest request, HttpServletResponse response,
      @PathVariable Map<String, String> params) throws IOException {

    return new RedirectView("slizaa", true);
  }

  @RequestMapping(value = { "/" })
  public RedirectView slizaaRoot(HttpServletRequest request, HttpServletResponse response,
      @PathVariable Map<String, String> params) throws IOException {

    return new RedirectView("slizaa", true);
  }

  @RequestMapping(value = { "/slizaa/**" })
  public void slizaa(HttpServletRequest request, HttpServletResponse response, @PathVariable Map<String, String> params)
      throws IOException {

    try (InputStream inputStream = new ClassPathResource("static/assets/slizaa/index.html").getInputStream()) {

      String content = StreamUtils.copyToString(inputStream, Charset.defaultCharset());
      content = content.replace("/slizaa/", "/assets/slizaa/");

      response.setContentType("text/html; charset=UTF-8");
      response.getOutputStream().write(content.getBytes());
    }
  }
}
