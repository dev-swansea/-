import {useCallback} from "react";
import {useNavigate} from "react-router-dom";

const ModifyPage = ({tno}) => {
  const navigate = useNavigate();

  const moveToRead = tno => {
    navigate({pathname: `/todo/read/${tno}`});
  };

  const moveToList = () => {
    navigate({pathname: `/todo/list `});
  };

  return <div className="text-3xl font-extrabold">ModifyPage</div>;
};

export default ModifyPage;
