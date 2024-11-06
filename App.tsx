import React from 'react';
import {SafeAreaView, Text} from 'react-native';
import type {StoreType} from './src/notifications';
import {resolveService} from './src/notifications';

type Props = {
  storeType: StoreType;
};

function applyFor<T>(item: T, block: (item: T) => void): T {
  block(item);

  return item;
}

const App: React.FC<Props> = ({storeType}) => {
  const [token, setToken] = React.useState('');
  React.useEffect(() => {
    async function init() {
      const service = await resolveService(storeType);

      if (!service) {
        return;
      }

      await service.initialzie();

      const fcmToken = await service.getToken();
      setToken(fcmToken);
    }

    init();
  }, []);

  return (
    <SafeAreaView>
      <Text>Token: {token}</Text>
    </SafeAreaView>
  );
};

export default App;
