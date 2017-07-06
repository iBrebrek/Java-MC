/**
 * Function used to avoid html injection.
 * 
 * http://stackoverflow.com/questions/1219860/html-encoding-in-javascript-jquery
 */

function htmlEscape(str) {
    return String(str)
            .replace(/&/g, '&amp;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#39;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;');
}
