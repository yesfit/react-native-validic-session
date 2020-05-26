
import { NativeModules, NativeEventEmitter } from 'react-native';

/**
 * @namespace
 * @description
 * The ValidicSession allows for managing and observing a Validic user session with the Validic Mobile SDK. Starting a 
 * session is a required step before interacting with any Validic Mobile SDKs. 
 */
var ValidicSession = {
    /**
     * A Validic User e
     * @typedef User 
     * @memberof ValidicSession
     * @property {String} user_id      The Validic User Id to submit data to
     * @property {String} org_id       The Validic Organization the user_id belongs to
     * @property {String} user_token   The authroization token for the Validic user_id
     */

    /**
     * Start a Session with a Validic User 
     * @param {User} user 
     */
    startSession(user) {
        NativeModules.RNValidicMobileSession.startSession(user.user_id, user.org_id, user.user_token);
    },

    /**
     * End a Validic user's session. This will remove any pending work from the Validic Mobile SDK
     */
    endSession() {
        NativeModules.RNValidicMobileSession.endSession();
    },

    /**
     * @callback UserCallback - The callback containing the User credentials as added in {@function startSession}
     * @memberof ValidicSession
     * or nothing if the user is empty
     * @property {User} user - The Validic User 
     */

    /**
     * @param {UserCallback} callback
     * @memberof ValidicSession
     */
    getUser(callback) { NativeModules.RNValidicMobileSession.getUser(callback) },

    /**
     * @property {String} version - The native session library version
     */
    version: NativeModules.RNValidicMobileSession.SESSION_VERSION,

    /**
     * Events emitted by the native modules can be subscribed to here. Listeners can be registered to at any 
     * place in the app but should be unregistered when no longer needed
     */
    eventEmitter: new NativeEventEmitter(NativeModules.RNValidicMobileSession)

};

export default ValidicSession;

