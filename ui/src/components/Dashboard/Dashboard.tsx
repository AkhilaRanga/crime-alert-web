import React from "react";
import { Button } from "@material-ui/core";
import { useNavigate } from "react-router-dom";
import { RouterPath } from "../../constants/routerConstants";

export const dashboardTestId = "dashboard-test-id";

function Dashboard() {
  const navigate = useNavigate();
  return (
    <div data-testid={dashboardTestId}>
      <div>User Dashboard placeholder</div>
      <Button
        variant="contained"
        color="primary"
        type="button"
        onClick={() => navigate(RouterPath.UPDATE_PROFILE)}
      >
        Update Profile
      </Button>
    </div>
  );
}

export default Dashboard;
