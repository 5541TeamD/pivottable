import React from 'react';
import { Segment, Icon } from 'semantic-ui-react';

const Footer = () => {
  return (
    <Segment as="footer" size="small" textAlign="right">
      D Pivot Table App <Icon name="list layout" /> Presented in {new Date().getFullYear()}
      by Team D
    </Segment>
  );
}

export default Footer;
