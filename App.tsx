/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import React from 'react';
import {Alert, SafeAreaView, ScrollView, Text, View} from 'react-native';
import {RemoteMessagingModule} from './src/notifications';
import {RemoteMessage} from './src/notifications';

const App: React.FC = () => {
  const [remoteToken, setRemoteToken] = React.useState('');
  const [receivedMessagesInForeground, setReceivedMessagesInForeground] =
    React.useState<RemoteMessage[]>([]);

  React.useEffect(() => {
    const init = async () => {
      try {
        const token = await RemoteMessagingModule.getToken();
        console.log(token);
        setRemoteToken(token);
      } catch (ex) {
        console.log(ex);
      }
    };

    init();

    const unsubscribeFromMessages = RemoteMessagingModule.onNewMessage(
      message => {
        console.log('there');
        setReceivedMessagesInForeground(prev => [...prev, message]);
      },
    );

    const unsubscribeFromOpened = RemoteMessagingModule.onMessageOpenedApp(
      message => {
        Alert.alert('Notification received', JSON.stringify(message));
      },
    );

    return () => {
      unsubscribeFromMessages();
      unsubscribeFromOpened();
    };
  }, []);

  return (
    <SafeAreaView>
      <ScrollView>
        <Text>Token: {remoteToken}</Text>
        {receivedMessagesInForeground.map(x => (
          <View key={x.id}>
            <Text>Received message {x.id}</Text>
            <Text>Title {x.notification?.title ?? 'No title'}</Text>
            <Text>Body {x.notification?.body ?? 'No body'}</Text>
          </View>
        ))}
      </ScrollView>
    </SafeAreaView>
  );
};

export default App;
