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
import {Icon} from 'antd';
import ApolloClient from 'apollo-client';
import * as React from 'react';
import {Query} from 'react-apollo';
import {Svg, SvgVariables} from "../../gqlqueries/__generated__/Svg";
import {GQ_GET_SVG} from "../../gqlqueries/GqlQueries";

const svgCache = new Map();

export interface ISlizaaIconProperties {
    iconId: string;
}

export function fetchSvg(client: ApolloClient<any>, iconId: string): Promise<string> {

    // return the cached instance if exist
    if (svgCache.has(iconId)) {
        return Promise.resolve(svgCache.get(iconId));
    }

    const result = client.query<Svg, SvgVariables>({
        query: GQ_GET_SVG,
        variables: {
            identifier: iconId
        }
    });

    return result.then(
        successCallback => successCallback.data.svg,
        failureCallback => '<svg viewBox="0 0 1000 1000" width="18px" height="18px" fill="currentColor">\n' +
            '<g>\n' +
            '<path d="M271.327,149.226c13.461-11.89,28.227-23.182,44.275-33.878c16.04-10.695,33.37-19.997,51.997-27.928\n' +
            'c18.62-7.923,38.819-14.162,60.613-18.715c21.787-4.555,45.363-6.836,70.721-6.836c34.468,0,65.86,4.747,94.194,14.259\n' +
            'c28.314,9.511,52.592,23.084,72.791,40.713c20.207,17.627,35.852,38.922,46.951,63.877c11.082,24.962,16.635,52.892,16.635,83.795\n' +
            'c0,30.113-4.359,56.156-13.074,78.145c-8.719,21.988-19.705,41.107-32.98,57.348c-13.275,16.25-27.631,30.308-43.082,42.196\n' +
            'c-15.453,11.881-30.113,23.076-43.979,33.571c-13.871,10.503-25.85,20.804-35.947,30.903c-10.109,10.1-16.152,21.496-18.127,34.168\n' +
            'l-13.672,86.77H428.212l-10.1-96.869c-0.402-1.975-0.596-3.658-0.596-5.053c0-1.379,0-3.064,0-5.045\n' +
            'c0-17.436,4.353-32.589,13.074-45.47c8.713-12.864,19.611-24.954,32.685-36.247c13.074-11.292,27.13-22.287,42.196-32.983\n' +
            'c15.047-10.695,29.111-22.286,42.188-34.764c13.074-12.478,23.969-26.639,32.684-42.486c8.713-15.847,13.082-34.473,13.082-55.865\n' +
            'c0-14.259-2.678-27.043-8.027-38.327c-5.346-11.292-12.777-20.997-22.287-29.122c-9.502-8.116-20.902-14.354-34.168-18.715\n' +
            'c-13.275-4.36-27.631-6.537-43.082-6.537c-22.585,0-41.704,2.475-57.349,7.424c-15.653,4.957-28.92,10.502-39.817,16.644\n' +
            'c-10.897,6.142-20.11,11.688-27.631,16.636c-7.527,4.957-14.267,7.432-20.207,7.432c-14.258,0-24.568-5.949-30.904-17.83\n' +
            'L271.327,149.226z M383.648,849.865c0-12.275,2.272-23.963,6.826-35.063c4.554-11.082,10.898-20.594,19.023-28.525\n' +
            'c8.116-7.922,17.724-14.26,28.823-19.016c11.083-4.754,22.972-7.131,35.651-7.131c12.276,0,23.963,2.377,35.061,7.131\n' +
            'c11.094,4.756,20.596,11.094,28.525,19.016c7.926,7.932,14.27,17.443,19.016,28.525c4.756,11.1,7.133,22.787,7.133,35.063\n' +
            'c0,12.686-2.377,24.471-7.133,35.361c-4.746,10.896-11.09,20.303-19.016,28.227c-7.93,7.932-17.432,14.168-28.525,18.715\n' +
            'c-11.097,4.555-22.785,6.836-35.061,6.836c-12.679,0-24.568-2.281-35.651-6.836c-11.099-4.547-20.707-10.783-28.823-18.715\n' +
            'c-8.125-7.924-14.469-17.33-19.023-28.227C385.92,874.336,383.648,862.551,383.648,849.865z"/>\n' +
            '</g>\n' +
            '</svg>');
}

export const SlizaaIcon = ({iconId}: ISlizaaIconProperties) => {

    // return the cached instance if exist
    if (svgCache.has(iconId)) {

        // tslint:disable-next-line:jsx-no-lambda
        return <Icon component={() => <div dangerouslySetInnerHTML={{__html: svgCache.get(iconId)}}/>}/>
    }

    return <Query<Svg, SvgVariables> query={GQ_GET_SVG} variables={{identifier: iconId}}>
        {({loading, error, data}) => {
            if (loading) {
                return <Icon component={DefaultSvg}/>;
            }
            if (error) {
                return <Icon component={DefaultSvg}/>
            }
            if (data && data.svg) {
                svgCache.set(iconId, data.svg);
                // tslint:disable-next-line:jsx-no-lambda
                return <Icon component={() => <div dangerouslySetInnerHTML={{__html: data.svg}}/>}/>
            } else {
                return <Icon component={DefaultSvg}/>
            }

        }}
    </Query>
}

const DefaultSvg = () => (
    <svg viewBox="0 0 1000 1000" width="18px" height="18px" fill="currentColor">
        <g>
            <path d="M271.327,149.226c13.461-11.89,28.227-23.182,44.275-33.878c16.04-10.695,33.37-19.997,51.997-27.928
                c18.62-7.923,38.819-14.162,60.613-18.715c21.787-4.555,45.363-6.836,70.721-6.836c34.468,0,65.86,4.747,94.194,14.259
                c28.314,9.511,52.592,23.084,72.791,40.713c20.207,17.627,35.852,38.922,46.951,63.877c11.082,24.962,16.635,52.892,16.635,83.795
                c0,30.113-4.359,56.156-13.074,78.145c-8.719,21.988-19.705,41.107-32.98,57.348c-13.275,16.25-27.631,30.308-43.082,42.196
                c-15.453,11.881-30.113,23.076-43.979,33.571c-13.871,10.503-25.85,20.804-35.947,30.903c-10.109,10.1-16.152,21.496-18.127,34.168
                l-13.672,86.77H428.212l-10.1-96.869c-0.402-1.975-0.596-3.658-0.596-5.053c0-1.379,0-3.064,0-5.045
                c0-17.436,4.353-32.589,13.074-45.47c8.713-12.864,19.611-24.954,32.685-36.247c13.074-11.292,27.13-22.287,42.196-32.983
                c15.047-10.695,29.111-22.286,42.188-34.764c13.074-12.478,23.969-26.639,32.684-42.486c8.713-15.847,13.082-34.473,13.082-55.865
                c0-14.259-2.678-27.043-8.027-38.327c-5.346-11.292-12.777-20.997-22.287-29.122c-9.502-8.116-20.902-14.354-34.168-18.715
                c-13.275-4.36-27.631-6.537-43.082-6.537c-22.585,0-41.704,2.475-57.349,7.424c-15.653,4.957-28.92,10.502-39.817,16.644
                c-10.897,6.142-20.11,11.688-27.631,16.636c-7.527,4.957-14.267,7.432-20.207,7.432c-14.258,0-24.568-5.949-30.904-17.83
                L271.327,149.226z M383.648,849.865c0-12.275,2.272-23.963,6.826-35.063c4.554-11.082,10.898-20.594,19.023-28.525
                c8.116-7.922,17.724-14.26,28.823-19.016c11.083-4.754,22.972-7.131,35.651-7.131c12.276,0,23.963,2.377,35.061,7.131
                c11.094,4.756,20.596,11.094,28.525,19.016c7.926,7.932,14.27,17.443,19.016,28.525c4.756,11.1,7.133,22.787,7.133,35.063
                c0,12.686-2.377,24.471-7.133,35.361c-4.746,10.896-11.09,20.303-19.016,28.227c-7.93,7.932-17.432,14.168-28.525,18.715
                c-11.097,4.555-22.785,6.836-35.061,6.836c-12.679,0-24.568-2.281-35.651-6.836c-11.099-4.547-20.707-10.783-28.823-18.715
                c-8.125-7.924-14.469-17.33-19.023-28.227C385.92,874.336,383.648,862.551,383.648,849.865z"/>
        </g>
    </svg>
);

// const RootSvg = () => (
//     <svg viewBox="0 0 1000 1000" width="18px" height="18px" fill="currentColor">
//         <path fill="#5B86C0" stroke="#000000" stroke-width="30" stroke-miterlimit="10" d="M250.5,188.833V821.5h0.022
//             c0.963,51.238,112.728,92.666,250.478,92.666c137.752,0,249.516-41.428,250.479-92.666h0.021V188.833H250.5z"/>
//         <ellipse fill="#7A9FCF" stroke="#000000" stroke-width="30" stroke-miterlimit="10" cx="501" cy="180.5" rx="250.5" ry="93.333"/>
//     </svg>
// );

export default SlizaaIcon;