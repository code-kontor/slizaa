/**
 * slizaa-server-staticcontent - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.staticcontent;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import io.codekontor.slizaa.server.service.svg.ISvgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SvgContentController {

  @Autowired
  private ISvgService _svgService;

  // the resource cache
  private ConcurrentHashMap<String, byte[]> _resourceCache = new ConcurrentHashMap<>();

//	@RequestMapping(value = "/svg/{main}", method = RequestMethod.GET)
//	public ResponseEntity<String> svg(@PathVariable String id,
//			@RequestParam(value = "ul", required = false) String upperLeft,
//			@RequestParam(value = "ur", required = false) String upperRight,
//			@RequestParam(value = "ll", required = false) String lowerLeft,
//			@RequestParam(value = "lr", required = false) String lowerRight) throws IOException {
//
//		System.out.println(main + ": " + upperLeft + " : " + upperRight + " : " + lowerLeft + " : " + lowerRight);
//
//		//
//		HttpHeaders headers = new HttpHeaders();
//		// TODO
//		headers.setCacheControl(CacheControl.noCache().getHeaderValue());
//		// TODO
//		headers.setContentType(MediaType.TEXT_XML);
//		ResponseEntity<String> responseEntity = new ResponseEntity<>("BUMM", headers, HttpStatus.OK);
//		return responseEntity;
//	}

  @RequestMapping(value = "/svg/{shortKey}", method = RequestMethod.GET)
  public ResponseEntity<String> svg(@PathVariable String shortKey) throws IOException {

    //
    HttpHeaders headers = new HttpHeaders();
    // TODO
    headers.setCacheControl(CacheControl.noCache().getHeaderValue());
    // TODO
    headers.setContentType(MediaType.TEXT_XML);
    String svgXml = _svgService.getSvg(shortKey);
    ResponseEntity<String> responseEntity = new ResponseEntity<>(svgXml, headers, HttpStatus.OK);
    return responseEntity;
  }
}
