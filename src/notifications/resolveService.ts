import {StoreType, IMessagingService} from './types';

export async function resolveService(
  storeType: StoreType,
): Promise<IMessagingService | null> {
  if (storeType === 'Google' || storeType === 'ios') {
    const Class = (await import('./Google')).GoogleNotificationsService;

    return new Class();
  }

  if (storeType === 'Huawei') {
    const Class = (await import('./Huawei')).HuaweiNotificationsService;

    return new Class();
  }

  return null;
}
