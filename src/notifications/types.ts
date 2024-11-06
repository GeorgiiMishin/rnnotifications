export type StoreType = 'Google' | 'Huawei' | 'ios';

export interface INotification {
  title: string;
  body: string;
}

export interface IMessage {
  id: string;
  data: Record<string, unknown>;
  notification?: INotification;
}

export interface IMessagingService {
  initialzie(): Promise<void>;
  getToken(): Promise<string>;
  getInitialMessage(): Promise<IMessage | null>;
  onMessageReceive(cb: (message: IMessage) => void): () => void;
  onTokenChange(cb: (token: string) => void): () => void;
}
