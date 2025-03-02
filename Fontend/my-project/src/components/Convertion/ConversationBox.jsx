import { useCallback, useEffect, useMemo, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import clsx from 'clsx';
import Avatar from '@/components/Avatar';
import ApiService from '../../services/apis';
import useOtherUser from '../../hooks/useOtherUser';
import { format, parseISO } from 'date-fns';
import usePhoneNumber from '../../hooks/usePhoneNumber';

const ConversationBox = ({ data, selected }) => {
  const otherUser = useOtherUser(data);
  const navigate = useNavigate();
  const { phone } = usePhoneNumber();

  // Chuyển trang khi click vào ConversationBox
  const handleClick = useCallback(() => {
    navigate(`/conversations/${data.id}`);
  }, [data.id, navigate]);

  // Lấy tin nhắn cuối cùng
  const lastMessage = useMemo(() => {
    const messages = data.messages || [];
    return messages[messages.length - 1];
  }, [data.messages]);

  // Lấy số điện thoại của user đăng nhập
  const userNumPhone = useMemo(() => {
    return phone;
  }, [phone]);

  const hasSeen = useMemo(() => {
    if (!lastMessage || !userNumPhone) {
      return false;
    }
    return (
      lastMessage.seenUsers?.some(
        (user) => user.phoneNumber === userNumPhone
      ) || false
    );
  }, [userNumPhone, lastMessage]);

  // Nội dung hiển thị của tin nhắn cuối
  const lastMessageText = useMemo(() => {
    if (lastMessage?.image) {
      return 'Sent an image';
    }
    if (lastMessage?.body) {
      return lastMessage.body;
    }
    return 'Start a conversations';
  }, [lastMessage]);

  return (
    <div
      onClick={handleClick}
      className={clsx(
        'w-full relative flex items-center space-x-3 hover:bg-neutral-100 transition cursor-pointer p-2',
        selected ? 'bg-[#DBEBFF]' : 'bg-white'
      )}
    >
      <Avatar user={otherUser} />
      <div className='min-w-0 flex-1'>
        <div className='focus:outline-none'>
          <div className='flex justify-between items-center mb-1'>
            <p className='text-md font-medium text-gray-900'>
              {data.name || otherUser.name}
            </p>
            {lastMessage?.createdAt && (
              <p className='text-xs text-gray-400 font-light'>
                {format(parseISO(lastMessage.createdAt), 'p')}
              </p>
            )}
          </div>
          <p
            className={clsx(
              'truncate text-sm',
              hasSeen ? 'text-black font-medium' : 'text-gray-700'
            )}
          >
            {lastMessageText}
          </p>
        </div>
      </div>
    </div>
  );
};

export default ConversationBox;
