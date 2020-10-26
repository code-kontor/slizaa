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
import { Icon } from 'antd';
import * as React from 'react';

export const DependencyVisualisation = () => (
  <svg x="0px" y="0px" width="16.422px" height="16.297px" viewBox="0 0 16.422 16.297" enableBackground="new 0 0 16.422 16.297">
    <rect x="0.5" y="0.5" fill="none" stroke="#FFFFFF" stroke-miterlimit="10" width="5.328" height="5.328" />
    <rect x="10.594" y="3.823" fill="none" stroke="#FFFFFF" stroke-miterlimit="10" width="5.328" height="5.328" />
    <rect x="3.865" y="10.469" fill="none" stroke="#FFFFFF" stroke-miterlimit="10" width="5.328" height="5.328" />
    <line fill="none" stroke="#FFFFFF" stroke-miterlimit="10" x1="5.828" y1="3.969" x2="10.672" y2="5.578" />
    <line fill="none" stroke="#FFFFFF" stroke-miterlimit="10" x1="3.938" y1="5.703" x2="5.813" y2="10.563" />
    <line fill="none" stroke="#FFFFFF" stroke-miterlimit="10" x1="8.953" y1="10.781" x2="10.797" y2="8.891" />
  </svg>
);

export const CrossReferencer = () => (
    <svg x="0px" y="0px" width="16.422px" height="16.297px" viewBox="0 0 16.422 16.297" enable-background="new 0 0 16.422 16.297">
        <polygon fill="none" stroke="#FFFFFF" stroke-miterlimit="10" points="14.838,4.039 11.151,1 11.151,2.542 1.857,2.542
	1.857,5.535 11.151,5.535 11.151,7.076 "/>
        <polygon fill="none" stroke="#FFFFFF" stroke-miterlimit="10" points="1.857,12.259 5.544,15.297 5.544,13.755 14.838,13.755
	14.838,10.762 5.544,10.762 5.544,9.22 "/>
    </svg>
);

export const CrossReferencerIcon = (props: any) => (
  <Icon component={CrossReferencer} {...props} />
);

export const DependencyVisualisationIcon = (props: any) => (
    <Icon component={DependencyVisualisation} {...props} />
);

export const SlizaaSvg = () => (
  <svg version="1.1" x="0px" y="0px" width="493.923px" height="175.948px" viewBox="0 0 493.923 175.948" enableBackground="new 0 0 493.923 175.948">
  <g>
    <rect x="94.293" y="96.048" fill="#40382B" width="31.84" height="39.616"/>
    <path fill="#40382B" d="M39.458,96.048c1.309,2.004,1.975,4.522,1.975,7.572c0,3.54-1.26,6.429-3.776,8.674
      c-2.519,2.246-5.816,3.368-9.899,3.368c-3.54,0-7.178-0.644-10.919-1.939c-3.744-1.292-6.633-2.755-8.674-4.387L0,129.541
      c3.537,2.315,8.164,4.22,13.879,5.714c5.714,1.497,11.837,2.247,18.369,2.247c12.789,0,22.961-3.298,30.513-9.899
      c7.552-6.599,11.328-14.865,11.328-24.799c0-2.374-0.219-4.618-0.617-6.756H39.458z"/>
    <path fill="#40382B" d="M353.91,96.048v21.859c-2.995,1.499-6.872,2.244-11.634,2.244c-6.531,0-11.5-1.494-14.9-4.489
      c-3.402-2.991-5.102-7.619-5.102-13.878c0-2.068,0.158-3.98,0.476-5.736h-29.702c-0.382,2.193-0.572,4.513-0.572,6.959
      c0,23.135,16.053,34.698,48.168,34.698c9.251,0,17.553-0.715,24.9-2.144s13.4-3.434,18.165-6.021V96.048H353.91z"/>
    <path fill="#40382B" d="M464.124,96.048v21.859c-2.994,1.499-6.873,2.244-11.633,2.244c-6.531,0-11.501-1.494-14.9-4.489
      c-3.402-2.991-5.104-7.619-5.104-13.878c0-2.068,0.159-3.98,0.477-5.736h-29.702c-0.382,2.193-0.573,4.513-0.573,6.959
      c0,23.135,16.055,34.698,48.169,34.698c9.251,0,17.553-0.715,24.901-2.144c7.347-1.429,13.4-3.434,18.164-6.021V96.048H464.124z"/>
    <polygon fill="#40382B" points="249.815,96.048 217.59,96.048 201.038,135.664 278.597,135.664 278.597,115.05 242.267,115.05 	"/>
    <rect x="151.441" y="96.048" fill="#40382B" width="31.84" height="39.616"/>
  </g>
  <g>
    <path fill="#40382B" d="M34.818,50.763c0.56-1.279,1.364-2.44,2.431-3.475c2.245-2.176,5.338-3.267,9.287-3.267
      c3.399,0,6.563,0.581,9.491,1.735c2.924,1.158,5.338,2.484,7.246,3.98l7.755-18.166c-8.572-5.307-19.118-7.96-31.635-7.96
      c-10.888,0-19.731,3.266-26.533,9.798c-5.138,4.93-8.332,10.717-9.59,17.354H34.818z"/>
    <polygon fill="#40382B" points="236.511,50.763 267.806,50.763 277.78,25.652 204.712,25.652 204.712,46.268 238.389,46.268 	"/>
    <rect x="151.441" y="25.652" fill="#40382B" width="31.84" height="25.11"/>
    <path fill="#40382B" d="M319.825,45.247c4.762-1.226,9.66-1.838,14.695-1.838c6.256,0,11.053,1.091,14.389,3.266
      c1.542,1.008,2.711,2.381,3.54,4.088h29.952C380.7,43.08,377.332,37.152,372.278,33c-7.621-6.257-18.234-9.389-31.84-9.389
      c-6.123,0-12.96,0.715-20.512,2.144s-14.525,3.504-20.92,6.226l6.939,18.164C310.437,48.104,315.06,46.472,319.825,45.247z"/>
    <path fill="#40382B" d="M430.039,45.247c4.762-1.226,9.659-1.838,14.695-1.838c6.257,0,11.054,1.091,14.39,3.266
      c1.541,1.008,2.711,2.381,3.539,4.088h29.953c-1.702-7.683-5.07-13.61-10.123-17.763c-7.621-6.257-18.235-9.389-31.84-9.389
      c-6.123,0-12.961,0.715-20.513,2.144s-14.526,3.504-20.921,6.226l6.94,18.164C420.65,48.104,425.274,46.472,430.039,45.247z"/>
    <rect x="94.293" fill="#40382B" width="31.84" height="50.763"/>
  </g>
  <polygon fill="#889696" points="234.142,56.433 219.959,90.378 252.067,90.378 265.554,56.433 "/>
  <path fill="#889696" d="M353.844,56.433c0.038,0.542,0.066,1.094,0.066,1.673v5.102c-19.186,1.771-34.223,5.582-45.106,11.431
    c-6.992,3.758-11.725,9.01-14.226,15.74h30.017c1.263-2.489,3.037-4.474,5.334-5.943c5.102-3.267,13.094-5.44,23.981-6.531v12.475
    h29.799V63.819c0-2.596-0.13-5.056-0.382-7.387H353.844z"/>
  <path fill="#889696" d="M464.058,56.433c0.038,0.542,0.066,1.094,0.066,1.673v5.102c-19.186,1.771-34.223,5.582-45.106,11.431
    c-6.991,3.758-11.725,9.01-14.226,15.74h30.016c1.264-2.489,3.039-4.474,5.334-5.943c5.103-3.267,13.096-5.44,23.982-6.531v12.475
    h29.799V63.819c0-2.596-0.129-5.056-0.381-7.387H464.058z"/>
  <rect x="151.441" y="56.433" fill="#889696" width="31.84" height="33.945"/>
  <path fill="#889696" d="M48.475,70.658c-9.194-3.922-14.037-8.663-14.543-14.226H2.678c-0.009,0.353-0.024,0.703-0.024,1.061
    c0,6.397,1.767,12.179,5.306,17.348c3.537,5.173,8.981,9.221,16.329,12.145c2.996,1.193,5.543,2.324,7.657,3.393h39.911
    c-0.851-2.124-1.939-4.108-3.278-5.943C64.905,79.401,58.201,74.81,48.475,70.658z"/>
  <rect x="94.293" y="56.433" fill="#889696" width="31.84" height="33.945"/>
</svg>
);
