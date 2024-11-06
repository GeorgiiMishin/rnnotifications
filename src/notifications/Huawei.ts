import {
  HmsPushInstanceId,
  HmsPushEvent,
  HmsPushMessaging,
  RNRemoteMessage,
} from '@hmscore/react-native-hms-push';

import {IMessage, INotification, IMessagingService} from './types';
import {getDefaultId} from './utils';

function convertToAppMessage(base: RNRemoteMessage): IMessage {
  const notification: INotification = {
    title: base.getNotificationTitle(),
    body: base.getBody(),
  };

  const message: IMessage = {
    id: base.getNotifyId().toString() || getDefaultId(),
    data: JSON.parse(base.getData() || '{}'),
    notification,
  };

  return message;
}

export class HuaweiNotificationsService implements IMessagingService {
  async initialzie(): Promise<void> {}

  getToken = async (): Promise<string> => {
    const tokenCtx = await HmsPushInstanceId.getToken('');

    return (tokenCtx as {result: string}).result;
  };

  async getInitialMessage(): Promise<IMessage | null> {
    const response = await HmsPushMessaging.getInitialNotification();

    const {result} = response as {result: RNRemoteMessage | undefined};
    if (!result) {
      return null;
    }

    return convertToAppMessage(result);
  }

  onMessageReceive(cb: (message: IMessage) => void): () => void {
    const remoteMessagesSubscriber = HmsPushEvent.onRemoteMessageReceived(
      (event: {msg: RNRemoteMessage}) => {
        cb(convertToAppMessage(event.msg));
      },
    ) as unknown as {remove: () => void};

    return () => remoteMessagesSubscriber.remove();
  }

  onTokenChange(cb: (token: string) => void): () => void {
    const listener = HmsPushEvent.onTokenReceived(
      ({token: newToken}: {token: string}) => {
        cb(newToken);
      },
    ) as unknown as {remove: () => void};

    return () => listener.remove();
  }
}
