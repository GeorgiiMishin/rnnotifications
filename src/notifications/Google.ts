import app from '@react-native-firebase/app';
import messaging from '@react-native-firebase/messaging';
import {Platform} from 'react-native';

import {IMessage, INotification, IMessagingService} from './types';
import {getDefaultId} from './utils';

const messagingInst = messaging();

function convertToAppMessage(
  base: NonNullable<
    Awaited<ReturnType<typeof messagingInst.getInitialNotification>>
  >,
): IMessage {
  const notification: INotification | undefined = base.notification
    ? {
        title: base.notification.title ?? '',
        body: base.notification.body ?? '',
      }
    : undefined;

  const message: IMessage = {
    id: base.messageId ?? getDefaultId(),
    data: base.data ?? {},
    notification,
  };

  return message;
}

export class GoogleNotificationsService implements IMessagingService {
  async initialzie(): Promise<void> {
    // For receive messages Android doesnt require notification permission, display only
    if (Platform.OS === 'ios') {
      await messagingInst.requestPermission();
    }
  }

  getToken = () => messagingInst.getToken();

  async getInitialMessage(): Promise<IMessage | null> {
    const initial = await messagingInst.getInitialNotification();

    if (!initial) {
      return null;
    }

    return convertToAppMessage(initial);
  }

  onMessageReceive(cb: (message: IMessage) => void): () => void {
    const unsubscribe = messagingInst.onMessage(message => {
      cb(convertToAppMessage(message));
    });

    return unsubscribe;
  }

  onTokenChange(cb: (token: string) => void): () => void {
    const unsubscribe = messagingInst.onTokenRefresh(token => {
      cb(token);
    });

    return unsubscribe;
  }
}
