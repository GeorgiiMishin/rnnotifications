export type Notification = {
  title: string;
  body: string;
  channelId: string;
};

export type RemoteMessage = {
  id: string;
  data: Record<string, string>;
  notification?: Notification;
};
