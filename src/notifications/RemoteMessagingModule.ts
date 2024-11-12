import {NativeModules, NativeEventEmitter} from 'react-native';
import {RemoteMessage} from './types';

const {RemoteMessagingModule: RemoteMessagingModuleNative} = NativeModules;

const eventEmitter = new NativeEventEmitter(RemoteMessagingModuleNative);

interface IModule {
  requestPermissions(): Promise<boolean>;
  getToken(): Promise<string>;
  getInitialNotification(): Promise<RemoteMessage>;
  onTokenChanged: (cb: (newToken: string) => void) => () => void;
  onMessageOpenedApp: (
    cb: (remoteMessage: RemoteMessage) => void,
  ) => () => void;
  onNewMessage: (cb: (remoteMessage: RemoteMessage) => void) => () => void;
}

export const RemoteMessagingModule: IModule = {
  requestPermissions: () => RemoteMessagingModuleNative.requestPermissions(),
  getToken: () => RemoteMessagingModuleNative.getToken(),
  getInitialNotification: () =>
    RemoteMessagingModuleNative.getInitialNotification(),
  onTokenChanged: cb => {
    const sub = eventEmitter.addListener('NewToken', ({token}) => {
      cb(token);
    });

    return () => sub.remove();
  },
  onMessageOpenedApp: cb => {
    const sub = eventEmitter.addListener('MessageOpened', message => {
      cb(message);
    });

    return () => sub.remove();
  },
  onNewMessage: cb => {
    const sub = eventEmitter.addListener('NewMessage', message => {
      cb(message);
    });

    return () => sub.remove();
  },
};
